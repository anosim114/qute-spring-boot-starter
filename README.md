![Build](https://github.com/anosim114/qute-spring-boot-starter/actions/workflows/maven.yml/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/net.snemeis/qute-spring-boot-starter.svg)](https://central.sonatype.com/artifact/net.snemeis/qute-spring-boot-starter)

<p align="center">
    <img src="https://github.com/anosim114/qute-spring-boot-starter/blob/master/_readme/qute_spring.png" alt="Logo" width="400" />
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

## Getting started

To run this project:

TODO

## Why Choose Qute over Thymeleaf?

- Qute supports slotting, thus suitable for building component libraries
- Qute has a good syntax

## Usage

Add required dependency

```xml
<dependency>
    <groupId>net.snemeis</groupId>
    <artifactId>qute-spring-boot-starter</artifactId>
    <version>VERSION</version>
</dependency>
```

The starter configures a `org.springframework.web.servlet.ViewResolver` ( in progress )
and instantiates a default qute engine callable with `Qute.engine()`.
Now you can return a string, pointing to template file name and the resolver will take care to instantiate the view and render the template.

> :info: By default, the templates are expected at src/main/resources/templates.

```qute
{!/templates/index.qute.html!}
{@java.lang.String name}

Hello {name}!
```

```java
// MainController.java

@GetMapping("/") 
public String index(Model model) {
    model.addAttribute("name", "World!");
    return "index";
}

/* alternatively */

@GetMapping("/") 
@ResponseBody
public String index(Model model) {
    model.addAttribute("name", "World!");
    
    return Qute.engine()
        .getTemplate("index")
        .render(model);
}
```

## Configuration

Original configuration and their explanations: [Qute Reference Guide 4.15. Configuration Reference](https://quarkus.io/guides/qute-reference#configuration-reference).

```properties
# default values, can be overwritten

# The list of suffixes used when attempting to locate a template file
spring.qute.suffixes=qute.html,qute.txt,html,txt
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
# TODO: The folder in which the templates live (classpath prefix)
spring.qute.prefix=/templates/
# TODO: Activate dev-mode (no-caching, hot-reloading)
spring.qute.dev-mode=false
# TODO: Set the dev-mode template prefix (used instead of classpath prefix)
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
{msg:some-key('some', 'params', 'for the message')}
{msg:some-other-key(oneVariable, 42)}
```

### Not supported features

- [3.5.8. User-defined Tags](https://quarkus.io/guides/qute-reference#user_tags)
