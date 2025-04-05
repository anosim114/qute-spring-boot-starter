package net.snemeis;

import io.quarkus.qute.CompletedStage;
import io.quarkus.qute.EvalContext;
import io.quarkus.qute.ValueResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public class TemplateExtensionValueResolver implements ValueResolver {
  private final Method method;

  @Override
  public boolean appliesTo(EvalContext context) {
    return context.getName().equals(method.getName());
  }

  @Override
  public CompletionStage<Object> resolve(EvalContext context) {
    var params = context.getParams().stream().map(param -> param.getLiteralValue().resultNow()).toList();
    Object[] args = Stream.concat(Stream.of(context.getBase()), params.stream()).toArray();

    try {
      var value = method.invoke(null, args);
      return CompletedStage.of(value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
