package net.snemeis;

import ch.qos.logback.core.util.StringUtil;
import io.quarkus.qute.EvalContext;
import io.quarkus.qute.NamespaceResolver;
import io.quarkus.qute.RawString;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HtmlNamespace {

  public static NamespaceResolver htmlNamespaceResolver() {
    return NamespaceResolver.builder("html")
      .resolve(ctx -> {
        if (ctx.getName().equals("attr")) {
          return htmlAttr(ctx);
        }  else if (ctx.getName().equals("class")) {
          return classAttr(ctx);
        }

        throw new RuntimeException("%s is not a valid html namespace method".formatted(ctx.getName()));
      })
      .build();
  }

  public static Object htmlAttr(EvalContext ctx) {
    if (ctx.getParams().size() != 2) {
      throw new RuntimeException("html:attr() got too many arguments, got %s expected 2".formatted(ctx.getParams().size()));
    }
    var nameParam = ctx.getParams().getFirst();
    var valueParam = ctx.getParams().get(1);

    var attrName = ctx.evaluate(nameParam).toCompletableFuture().resultNow();
    var attrValue = ctx.evaluate(valueParam).toCompletableFuture().resultNow();

    if (attrName == null || attrValue == null) {
      return "";
    }

    return new RawString("%s=\"%s\"".formatted(attrName, attrValue));
  }

  public static Object classAttr(EvalContext ctx) {
    String values = ctx.getParams()
      .stream()
      .map(ctx::evaluate)
      .map(value -> value.toCompletableFuture().resultNow())
      .filter(Objects::nonNull)
      .map(Object::toString)
      .collect(Collectors.joining(" "));

    return new RawString("class=\"%s\"".formatted(values));
  }
}
