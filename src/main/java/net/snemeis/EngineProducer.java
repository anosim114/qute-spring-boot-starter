package net.snemeis;

import io.quarkus.qute.*;
import io.quarkus.qute.TemplateLocator.TemplateLocation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.ViewResolver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@AutoConfiguration(after = { WebMvcAutoConfiguration.class, WebFluxAutoConfiguration.class })
@EnableConfigurationProperties(QuteProperties.class)
public class EngineProducer {

    private static final Logger log = LoggerFactory.getLogger(EngineProducer.class);

    public static final String INJECT_NAMESPACE = "inject";
    public static final String CDI_NAMESPACE = "cdi";
    public static final String MSG_NAMESPACE = "msg";

    private final ApplicationContext context; // TODO: rename context, as spring would do it
    private final QuteProperties config;
    private final Engine engine;
    private final ApplicationContext applicationContext;

    public EngineProducer(
            QuteProperties config,
            List<SectionHelperFactory<?>> sectionHelperFactories,
            List<ValueResolver> valueResolvers,
            List<NamespaceResolver> namespaceResolvers,
            List<ParserHook> parserHooks,
            ApplicationContext context
    ) {
        this.config = config;
        this.context = context;

        log.debug("initializing something in qute starter");

        EngineBuilder builder = Engine.builder();

        // We don't register the map resolver because of param declaration validation
        builder.addValueResolver(ValueResolvers.thisResolver());
        builder.addValueResolver(ValueResolvers.orResolver());
        builder.addValueResolver(ValueResolvers.trueResolver());
        builder.addValueResolver(ValueResolvers.collectionResolver());
        builder.addValueResolver(ValueResolvers.mapperResolver());
        builder.addValueResolver(ValueResolvers.mapEntryResolver());
        builder.addValueResolver(ValueResolvers.mapResolver());
        // foo.string.raw returns a RawString which is never escaped
        builder.addValueResolver(ValueResolvers.rawResolver()); // TODO: look into this for learning
        builder.addValueResolver(ValueResolvers.logicalAndResolver());
        builder.addValueResolver(ValueResolvers.logicalOrResolver());
        builder.addValueResolver(ValueResolvers.orEmpty());
        // Note that arrays are handled specifically during validation
        builder.addValueResolver(ValueResolvers.arrayResolver());
        // Additional user-provided value resolvers
        for (ValueResolver valueResolver : valueResolvers) {
            builder.addValueResolver(valueResolver);
        }

        // Enable/disable strict rendering
        if (config.strictRendering) {
            builder.strictRendering(true);
        } else {
            builder.strictRendering(false);

            builder.addResultMapper(new PropertyNotFoundThrowException());
        }

        // Escape some characters for HTML/XML templates
        builder.addResultMapper(new HtmlEscaper(List.copyOf(config.escapeContentTypes)));

        // Fallback reflection resolver
        builder.addValueResolver(new ReflectionValueResolver());

        // Remove standalone lines if desired
        builder.removeStandaloneLines(config.removeStandaloneLines);

        // TODO: what's this?
        // Iteration metadata prefix
        builder.iterationMetadataPrefix(config.iterationMetadataPrefix);

        // Default section helpers
        builder.addDefaultSectionHelpers();

        // Additional section helpers
        for (SectionHelperFactory<?> sectionHelperFactory : sectionHelperFactories) {
            builder.addSectionHelper(sectionHelperFactory);
        }

        // Resolve @Named beans
        builder.addNamespaceResolver(NamespaceResolver.builder(INJECT_NAMESPACE).resolve(this::resolveInject).build());
        builder.addNamespaceResolver(NamespaceResolver.builder(CDI_NAMESPACE).resolve(this::resolveInject).build());
        builder.addNamespaceResolver(NamespaceResolver.builder(MSG_NAMESPACE).resolve(this::resolveMessage).build());
        // Additional namespace resolvers
        for (NamespaceResolver namespaceResolver : namespaceResolvers) {
            builder.addNamespaceResolver(namespaceResolver);
        }

        // Add generated resolvers
        for (String resolverClass : config.resolverClasses) {
            Resolver resolver = createResolver(resolverClass);
            if (resolver instanceof NamespaceResolver) {
                builder.addNamespaceResolver((NamespaceResolver) resolver);
            } else {
                builder.addValueResolver((ValueResolver) resolver);
            }
//            log.debug("Added generated value resolver: {}", resolverClass);
            System.out.println("added some value resolver");
        }

        // Add locator
        builder.addLocator(this::locate);
        // registerCustomLocators(builder, locators); // custom locators

        // Add parser hooks
        for (ParserHook parserHook : parserHooks) {
            builder.addParserHook(parserHook);
        }
        // Add a special parser hook for Qute.fmt() methods
        builder.addParserHook(new Qute.IndexedArgumentsParserHook());

        // Add global providers
//        TODO: do this
//        for (String globalProviderClass : context.getTemplateGlobalProviderClasses()) {
//            TemplateGlobalProvider provider = createGlobalProvider(globalProviderClass);
//            builder.addTemplateInstanceInitializer(provider);
//            builder.addNamespaceResolver(provider);
//        }

        builder.timeout(config.timeout);
        builder.useAsyncTimeout(config.useAsyncTimeout);

        // Set the engine instance
        this.engine = builder.build();

        if (!config.cachingEnabled) {
            Qute.disableCache();
        }

        Qute.setEngine(engine);
        this.applicationContext = context;
    }

    @Bean
    ViewResolver quteViewResolver() {
        return new QuteViewResolver(config.cachingEnabled);
    }

    private Resolver createResolver(String resolverClassName) {
        try {
            Class<?> resolverClazz = getClass().getClassLoader().loadClass(resolverClassName);
            if (Resolver.class.isAssignableFrom(resolverClazz)) {
                return (Resolver) resolverClazz.getDeclaredConstructor().newInstance();
            }
            throw new IllegalStateException("Not a resolver: " + resolverClassName);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new IllegalStateException("Unable to create resolver: " + resolverClassName, e);
        }
    }

    // TODO: fix this, as global providers basically provide global variables
//    private TemplateGlobalProvider createGlobalProvider(String initializerClassName) {
//        try {
//            Class<?> initializerClazz = Thread.currentThread()
//                    .getContextClassLoader().loadClass(initializerClassName);
//            if (TemplateGlobalProvider.class.isAssignableFrom(initializerClazz)) {
//                return (TemplateGlobalProvider) initializerClazz.getDeclaredConstructor().newInstance();
//            }
//            throw new IllegalStateException("Not a global provider: " + initializerClazz);
//        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException
//                 | InvocationTargetException | NoSuchMethodException | SecurityException e) {
//            throw new IllegalStateException("Unable to create global provider: " + initializerClassName, e);
//        }
//    }

    private Optional<TemplateLocation> locate(String path) {
        log.debug("locating template {}", path);
        // TODO: test that    devmode resolves files by filepath
        // TODO: test that no devmode resolves files by classpath
        // if path is explicitly excluded
        if (config.templatePathExclude.matcher(path).matches()) {
            log.debug("skipping template because of template-exclude-path config");
            return Optional.empty();
        }

        // if dev-mode try resolving via filepath
        if (config.devMode) {
            String templatePath = config.devPrefix + path;
            log.debug("Resolving file-mode template: {}", templatePath);

            for (String suffix : config.suffixes) {
                String pathWithSuffix = templatePath + suffix;

                File file = new File(pathWithSuffix);
                if (!file.exists() && file.isDirectory()) {
                    continue;
                }

                try {
                    var content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    return Optional.of(new ContentTemplateLocation(content, createVariant(path)));

                } catch (Exception ex) {
                    continue;
                }
            }
        }

        // TODO: fill this via config parameter
        List<String> devIgnoreFilePath = List.of("lib1", "lib2");
        // TODO: properly calculate and actullay use
        boolean isExclusivelyInClassPathForExampleFragments = false;

        // First try to locate file-based templates
        String templatePath = config.prefix + path;
        log.debug("Resolving class-mode template: {}", templatePath);

        // Try path with suffixes
        for (String suffix : config.suffixes) {
            templatePath = config.prefix + path + suffix;

            URL resourceUrl = getResourceAsUrl(templatePath);
            if (resourceUrl != null) {
                return Optional.of(new ResourceTemplateLocation(resourceUrl, createVariant(templatePath)));
            }
        }

        return Optional.empty();
    }

    private URL getResourceAsUrl(String path) {
        try {
            var resource = Objects.requireNonNull(getClass().getResource(path));
            return resource.toURI().toURL();
        } catch (Exception e) {
            return null;
        }
    }

    private Variant createVariant(String path) {
        String contentType;
        try {
            contentType = Files.probeContentType(Path.of(path));
        } catch (Exception ex) {
            log.warn("could not determine content type of template");
            contentType = "application/octet-stream";
        }

        // TODO: use localeContextHolder?
        return new Variant(config.defaultLocale, config.defaultCharset, contentType);
    }

    private Object resolveInject(EvalContext ctx) {
        if (context.containsBean(ctx.getName())) {
            return context.getBean(ctx.getName());
        }

        return Results.NotFound.from(ctx);
    }

    private Object resolveMessage(EvalContext ctx) {
        Locale locale = LocaleContextHolder.getLocale();
        var params = ctx.getParams()
                .stream()
                .map(ctx::evaluate)
                .map(CompletionStage::toCompletableFuture)
                .map(CompletableFuture::resultNow)
                .toList();

        var key = (String) params.getFirst();
        List<Object> args = new ArrayList<>();

        if (params.size() > 1) {
            args = params.subList(1, params.size());
        }

        return applicationContext.getMessage(key, args.toArray(), locale);
    }

    static class ResourceTemplateLocation implements TemplateLocation {

        private final URL resource;
        private final Optional<Variant> variant;

        ResourceTemplateLocation(URL resource, Variant variant) {
            this.resource = resource;
            this.variant = Optional.ofNullable(variant);
        }

        @Override
        public Reader read() {
            Charset charset = null;
            if (variant.isPresent()) {
                charset = variant.get().getCharset();
            }
            if (charset == null) {
                charset = StandardCharsets.UTF_8;
            }
            try {
                return new InputStreamReader(resource.openStream(), charset);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public Optional<Variant> getVariant() {
            return variant;
        }

    }

    static class ContentTemplateLocation implements TemplateLocation {

        private final String content;
        private final Optional<Variant> variant;

        ContentTemplateLocation(String content, Variant variant) {
            this.content = content;
            this.variant = Optional.ofNullable(variant);
        }

        @Override
        public Reader read() {
            return new StringReader(content);
        }

        @Override
        public Optional<Variant> getVariant() {
            return variant;
        }

    }
}
