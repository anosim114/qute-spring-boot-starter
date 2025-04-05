![Build](https://github.com/anosim114/qute-spring-boot-starter/actions/workflows/maven.yml/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/net.snemeis/qute-spring-boot-starter.svg)](https://central.sonatype.com/artifact/net.snemeis/qute-spring-boot-starter)

<p align="center">
    <img src="/images/qute-spring.png" alt="Logo" width="400" />
</p>

<h1 align="center">Qute Spring Boot Starter</h1> 

> Qute is primarily designed as a Quarkus extension.
> It is possible to use it as a "standalone" library too.
> However, in such case some of the features are not available.
> In general, any feature mentioned under the [Quarkus Integration](https://huifer.github.io/quarkus-document/generated-docs/qute-reference.html#quarkus_integration)
> section is missing.
> You can find more information about the limitations and possibilities in the
> [Qute Used as a Standalone](https://huifer.github.io/quarkus-document/generated-docs/qute-reference.html#standalone)
> Library section.

Sure thing, here we go!

This project is in its core a port of the Quarkus Qute configuration to Spring Boot
enabling various Spring specific features to be used instead of Quarkus's.
It demonstrates how to set up and use Qute in a Spring Boot application,
offering a better alternative to other templating engines like for example Thymeleaf.

```qute
{@String name = 'World'}

{#include layout}

  {#title}A Qute Readme{/title}
  
  {#content}
    <h1>
        Hello {name}!
    </h1>
    
    <p>{msg:t('readme.welcome-message')}</p>
  {/content}
  
{/include}
```

## Why you should use Qute as a templating language

- Qute supports implementations of component libraries
(see [example-component-lib](/example-component-lib)
and [example-backend](/example-backend) as reference implementation)
- Qute natively supports slotting (unlike thymeleaf)
- Qute has a pleasant syntax (unlike thymeleaf)
- Qute supports improved developer-experience (ex. hot-reloading capabilities)
- Qute is backed by major corporations (whoever works on Quarkus)
- Qute is actively maintained (unlike thymeleaf)

For a rough overview of Qute's features read the 
[Qute Reference Guide](https://quarkus.io/guides/qute-reference)

## Usage

Add required dependency

```xml
<dependency>
    <groupId>net.snemeis</groupId>
    <artifactId>qute-spring-boot-starter</artifactId>
    <version>${version.qute-starter}</version>
</dependency>
```

The starter then autoconfigures a Qute engine instance and a template ViewResolver.

### Using the ViewResolver

```qute
{!/templates/index.qute.html!}
{@java.lang.String name}

Hello {name}!
```

```java
// MainController.java

@GetMapping("/") 
public String index(Model model) {
    model.addAttribute("name", "World");
    return "index";
}
```

### Using the Qute engine directly

Alternatively to resolving templates using the ViewResolver the Engine instance
can also be accessed directly by calling `Qute.engine()`.\
One notable difference is that the template cache is not being cleared
when the cache is turned off via `spring.qute.cache-enabled=false`.

```java
@GetMapping("/") 
@ResponseBody
public String index() {
    return Qute.engine()
        .getTemplate("index")
        .data(Map.of("name", "World"))
        .render();
}

// or

@GetMapping("/") 
@ResponseBody
public String index() {
    return Qute.fmt('Hello {name}!')
        .data(Map.of("name", "World"))
        .render();
}
```

## Configuration

For all configuration options of this starter take a look at the
[qute properties](https://github.com/anosim114/qute-spring-boot-starter/blob/master/src/main/java/net/snemeis/QuteProperties.java)
file.

For the quarkus configuration and their explanations: [Qute Reference Guide 4.15. Configuration Reference](https://quarkus.io/guides/qute-reference#configuration-reference).
(The ones for this starter are leaned on the original implementation).

Example configuration for local development:
```properties
# to load templates from filepath instead of classpath (direct editing feedback)
# good if coupled with something like:
# spring.resources.static-locations=file:///${user.dir}/src/main/resources/static/
spring.qute.dev-mode = true
# set path to filepath
spring.qute.dev-prefix = ${user.dir}/src/main/resources/templates/
# for hot-reloading in dev-mode
# (tip: good when coupled with custom vite config to force page reload on file save
# https://github.com/ElMassimo/vite-plugin-full-reload, see laravel using this for example)
spring.qute.caching-enabled = false
# to be able to resolve filenames like 'index', 'index.html' or 'index.qute.html'
spring.qute.suffixes = ,.html,.qute.html
# to get parsing mistakes in the templates
logging.level.io.quarkus=DEBUG
```

## Adding value and namespace resolvers

Beans of type `ValueResolver` and `NamespaceResolver` are collected
and registered as resolvers on initialization.
(As well as `ParserHooks` because the original implementation had this as well)

Example:
```java
@Configuration
class ValueResolverConfig {
  @Bean
  ValueResolver valueResolverA() {
      return new ValueResolverA();
  }
}
```

## Adding Template Extensions (or helper methods in a RoR sense)
Like in the quarkus qute reference guide described, classes annotated with 
`@TemplateExtension` are collected at startup and registered.
Current rules are that  extension methods must be static and only the bare
annotation is supported, so no annotation arguments.

```java
@TemplateExtension
class StringExtensions {
  public static String capitalize(String str) {
    return str.toUpperCase();
  }
}
```

```html
{@java.lang.String humblebeeSounds} <!-- 'bzzzzzzzzzz' -->
The humble-bee goes: "{humblebeeSounds.capitalize}". <!-- transformed to 'BZZZZZZZZZZ' -->
```
## Reference

For a general reference see the [Qute Reference Guide](https://quarkus.io/guides/qute-reference).

The spring related changes are the following.

### Qute Reference Guide 4.3. Injecting Beans Directly In Templates

Since Spring Boot also has beans which can be referenced at runtime, this feature is also supported
via the `cdi` and `inject` namespaces. So the following snippet is 100% valid:
```qute
{cdi:someService.findSomeValue(10).name} 
{inject:otherService.fetchSomethingElse()}
```

### Qute Reference Guide 4.14. Type-safe Message Bundles

This starter does not support type-safe message bundles,
however some part of the syntax to retrieve messages
from a message bundle is still valid:

```qute
{msg:t('some-key', 'with', 'params', 'for the message')}
{msg:t('some-other-key', oneVariable, 42)}
```
