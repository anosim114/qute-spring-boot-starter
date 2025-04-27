package io.quarkus.qute;

import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {EngineProducer.class, GlobalNamespaceResolverTest.GlobalNameSpaceConfiguration.class})
public class GlobalNamespaceResolverTest {

  Engine engine;

  @BeforeEach
  void initEngine() {
    this.engine = Qute.engine();
  }

  @Test
  public void tesNamespaceResolver() {
    assertEquals("bar", engine.parse("{global:foo('bar')}").render(null));
  }

  @TestConfiguration
  static class GlobalNameSpaceConfiguration {

    @Bean
    public NamespaceResolver globalNamespace() {
      return new NamespaceResolver() {

        @Override
        public CompletionStage<Object> resolve(EvalContext context) {
          if (!context.getName().equals("foo")) {
            return Results.notFound(context);
          }
          CompletableFuture<Object> ret = new CompletableFuture<>();
          context.evaluate(context.getParams().get(0)).whenComplete((r, e) -> {
            ret.complete(r);
          });
          return ret;
        }

        @Override
        public String getNamespace() {
          return "global";
        }
      };
    }
  }
}
