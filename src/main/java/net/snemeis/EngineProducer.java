/*
 * Qute Spring Boot Starter
 * Copyright 2025 anosim114
 *
 * This product contains pieces of code developed
 * for Quarkus (https://github.com/quarkusio/quarkus)
 *
 * The initial Developer for the copied, derived or inspired work of
 * src/main/java/net/snemeis/EngineProducer and
 * src/main/java/net/snemeis/PropertyNotFoundThrowException
 * is the Quarkus Project (https://github.com/quarkusio/quarkus).
 * https://github.com/quarkusio/quarkus/blob/main/extensions/qute/runtime/src/main/java/io/quarkus/qute/runtime/EngineProducer.java
 *
                                 Apache License
                           Version 2.0, January 2004
                        https://www.apache.org/licenses/

   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

   1. Definitions.

      "License" shall mean the terms and conditions for use, reproduction,
      and distribution as defined by Sections 1 through 9 of this document.

      "Licensor" shall mean the copyright owner or entity authorized by
      the copyright owner that is granting the License.

      "Legal Entity" shall mean the union of the acting entity and all
      other entities that control, are controlled by, or are under common
      control with that entity. For the purposes of this definition,
      "control" means (i) the power, direct or indirect, to cause the
      direction or management of such entity, whether by contract or
      otherwise, or (ii) ownership of fifty percent (50%) or more of the
      outstanding shares, or (iii) beneficial ownership of such entity.

      "You" (or "Your") shall mean an individual or Legal Entity
      exercising permissions granted by this License.

      "Source" form shall mean the preferred form for making modifications,
      including but not limited to software source code, documentation
      source, and configuration files.

      "Object" form shall mean any form resulting from mechanical
      transformation or translation of a Source form, including but
      not limited to compiled object code, generated documentation,
      and conversions to other media types.

      "Work" shall mean the work of authorship, whether in Source or
      Object form, made available under the License, as indicated by a
      copyright notice that is included in or attached to the work
      (an example is provided in the Appendix below).

      "Derivative Works" shall mean any work, whether in Source or Object
      form, that is based on (or derived from) the Work and for which the
      editorial revisions, annotations, elaborations, or other modifications
      represent, as a whole, an original work of authorship. For the purposes
      of this License, Derivative Works shall not include works that remain
      separable from, or merely link (or bind by name) to the interfaces of,
      the Work and Derivative Works thereof.

      "Contribution" shall mean any work of authorship, including
      the original version of the Work and any modifications or additions
      to that Work or Derivative Works thereof, that is intentionally
      submitted to Licensor for inclusion in the Work by the copyright owner
      or by an individual or Legal Entity authorized to submit on behalf of
      the copyright owner. For the purposes of this definition, "submitted"
      means any form of electronic, verbal, or written communication sent
      to the Licensor or its representatives, including but not limited to
      communication on electronic mailing lists, source code control systems,
      and issue tracking systems that are managed by, or on behalf of, the
      Licensor for the purpose of discussing and improving the Work, but
      excluding communication that is conspicuously marked or otherwise
      designated in writing by the copyright owner as "Not a Contribution."

      "Contributor" shall mean Licensor and any individual or Legal Entity
      on behalf of whom a Contribution has been received by Licensor and
      subsequently incorporated within the Work.

   2. Grant of Copyright License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      copyright license to reproduce, prepare Derivative Works of,
      publicly display, publicly perform, sublicense, and distribute the
      Work and such Derivative Works in Source or Object form.

   3. Grant of Patent License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      (except as stated in this section) patent license to make, have made,
      use, offer to sell, sell, import, and otherwise transfer the Work,
      where such license applies only to those patent claims licensable
      by such Contributor that are necessarily infringed by their
      Contribution(s) alone or by combination of their Contribution(s)
      with the Work to which such Contribution(s) was submitted. If You
      institute patent litigation against any entity (including a
      cross-claim or counterclaim in a lawsuit) alleging that the Work
      or a Contribution incorporated within the Work constitutes direct
      or contributory patent infringement, then any patent licenses
      granted to You under this License for that Work shall terminate
      as of the date such litigation is filed.

   4. Redistribution. You may reproduce and distribute copies of the
      Work or Derivative Works thereof in any medium, with or without
      modifications, and in Source or Object form, provided that You
      meet the following conditions:

      (a) You must give any other recipients of the Work or
          Derivative Works a copy of this License; and

      (b) You must cause any modified files to carry prominent notices
          stating that You changed the files; and

      (c) You must retain, in the Source form of any Derivative Works
          that You distribute, all copyright, patent, trademark, and
          attribution notices from the Source form of the Work,
          excluding those notices that do not pertain to any part of
          the Derivative Works; and

      (d) If the Work includes a "NOTICE" text file as part of its
          distribution, then any Derivative Works that You distribute must
          include a readable copy of the attribution notices contained
          within such NOTICE file, excluding those notices that do not
          pertain to any part of the Derivative Works, in at least one
          of the following places: within a NOTICE text file distributed
          as part of the Derivative Works; within the Source form or
          documentation, if provided along with the Derivative Works; or,
          within a display generated by the Derivative Works, if and
          wherever such third-party notices normally appear. The contents
          of the NOTICE file are for informational purposes only and
          do not modify the License. You may add Your own attribution
          notices within Derivative Works that You distribute, alongside
          or as an addendum to the NOTICE text from the Work, provided
          that such additional attribution notices cannot be construed
          as modifying the License.

      You may add Your own copyright statement to Your modifications and
      may provide additional or different license terms and conditions
      for use, reproduction, or distribution of Your modifications, or
      for any such Derivative Works as a whole, provided Your use,
      reproduction, and distribution of the Work otherwise complies with
      the conditions stated in this License.

   5. Submission of Contributions. Unless You explicitly state otherwise,
      any Contribution intentionally submitted for inclusion in the Work
      by You to the Licensor shall be under the terms and conditions of
      this License, without any additional terms or conditions.
      Notwithstanding the above, nothing herein shall supersede or modify
      the terms of any separate license agreement you may have executed
      with Licensor regarding such Contributions.

   6. Trademarks. This License does not grant permission to use the trade
      names, trademarks, service marks, or product names of the Licensor,
      except as required for reasonable and customary use in describing the
      origin of the Work and reproducing the content of the NOTICE file.

   7. Disclaimer of Warranty. Unless required by applicable law or
      agreed to in writing, Licensor provides the Work (and each
      Contributor provides its Contributions) on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
      implied, including, without limitation, any warranties or conditions
      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
      PARTICULAR PURPOSE. You are solely responsible for determining the
      appropriateness of using or redistributing the Work and assume any
      risks associated with Your exercise of permissions under this License.

   8. Limitation of Liability. In no event and under no legal theory,
      whether in tort (including negligence), contract, or otherwise,
      unless required by applicable law (such as deliberate and grossly
      negligent acts) or agreed to in writing, shall any Contributor be
      liable to You for damages, including any direct, indirect, special,
      incidental, or consequential damages of any character arising as a
      result of this License or out of the use or inability to use the
      Work (including but not limited to damages for loss of goodwill,
      work stoppage, computer failure or malfunction, or any and all
      other commercial damages or losses), even if such Contributor
      has been advised of the possibility of such damages.

   9. Accepting Warranty or Additional Liability. While redistributing
      the Work or Derivative Works thereof, You may choose to offer,
      and charge a fee for, acceptance of support, warranty, indemnity,
      or other liability obligations and/or rights consistent with this
      License. However, in accepting such obligations, You may act only
      on Your own behalf and on Your sole responsibility, not on behalf
      of any other Contributor, and only if You agree to indemnify,
      defend, and hold each Contributor harmless for any liability
      incurred by, or claims asserted against, such Contributor by reason
      of your accepting any such warranty or additional liability.

   END OF TERMS AND CONDITIONS

   APPENDIX: How to apply the Apache License to your work.

      To apply the Apache License to your work, attach the following
      boilerplate notice, with the fields enclosed by brackets "[]"
      replaced with your own identifying information. (Don't include
      the brackets!)  The text should be enclosed in the appropriate
      comment syntax for the file format. We also recommend that a
      file or class name and description of purpose be included on the
      same "printed page" as the copyright notice for easier
      identification within third-party archives.

   Copyright [yyyy] [name of copyright owner]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
            log.debug("Resolving file-mode template: {}", config.devPrefix + path);

            for (String suffix : config.suffixes) {
                String templatePath = config.devPrefix + path + suffix;

                File file = new File(templatePath);
                if (!file.exists() && file.isDirectory()) {
                    continue;
                }

                try {
                    var content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    return Optional.of(new ContentTemplateLocation(content, createVariant(templatePath)));

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
