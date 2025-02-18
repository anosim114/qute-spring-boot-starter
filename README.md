![Build](https://github.com/anosim114/qute-spring-boot-starter/actions/workflows/maven.yml/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/net.snemeis/qute-spring-boot-starter.svg)](https://central.sonatype.com/artifact/net.snemeis/qute-spring-boot-starter)

<p align="center">
    <img src="/images/qute_spring.png" alt="Logo" width="400" />
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

> :info: By default, the templates are expected at /resources/templates/.

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
```

## Configuration

Original configuration and their explanations: [Qute Reference Guide 4.15. Configuration Reference](https://quarkus.io/guides/qute-reference#configuration-reference).

```properties
# default values, can be overwritten

# The list of suffixes used when attempting to locate a template file
# Notice: the first entry is an empty '' resulting in the possibility of providing the full file name, e.g. 'template.qute.json'
spring.qute.suffixes=,qute.html,qute.txt,html,txt
# The folder in which the templates reside in
spring.qute.prefix=/templates/
# This regular expression is used to exclude template files from the templates directory
spring.qute.template-path-exclude=^\..|.\/\..*$
# The list of content types for which the ', ", <, > and & characters are escaped
spring.qute.escapeContentTypes=text/html,text/xml,application/xml,application/xhtml+xml
# The default charset of the templates files
spring.qute.default-charset=UTF-8
# Specify whether the parser should remove standalone lines from the output
spring.qute.remove-standalone-lines=true
# If set to true then Results.NotFound values will always result in a TemplateException and the rendering is aborted.
spring.qute.strict-rendering=true
# Activate dev-mode (file-path used for template resolving)
spring.qute.dev-mode=false
# Set the dev-mode template prefix (file path is being used instead of classpath)
# for better hot-reload capabilities
spring.qute.dev-prefix=${user.dir}/src/main/resources/templates/
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
