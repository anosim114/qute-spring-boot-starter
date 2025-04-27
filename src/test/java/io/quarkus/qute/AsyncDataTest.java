package io.quarkus.qute;

import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {EngineProducer.class, AsyncDataTest.ValueResolverConfig.class})
public class AsyncDataTest {
  Engine engine;

  @BeforeEach
  void initEngine() {
    this.engine = Qute.engine();
  }

  @Test
  public void testAsyncData() {
    assertEquals("alpha:bravo:delta:",
      engine.parse("{#for token in client.tokens}{token}:{/for}").data("client", new Client()).render());
    assertEquals("alpha:bravo:delta:",
      engine.parse("{#for token in tokens}{token}:{/for}").data("tokens", new Client().getTokens()).render());
    assertEquals("alpha", engine.parse("{token}").data("token", CompletedStage.of("alpha")).render());
  }

  static class Client {
    public CompletionStage<List<String>> getTokens() {
      CompletableFuture<List<String>> tokens = new CompletableFuture<>();
      tokens.complete(Arrays.asList("alpha", "bravo", "delta"));
      return tokens;
    }
  }

  @TestConfiguration
  static class ValueResolverConfig {

    @Bean
    public ValueResolver someResolver () {
      return ValueResolver.builder()
        .applyToBaseClass(Client.class)
        .applyToName("tokens")
        .resolveSync(ec -> ((Client) ec.getBase()).getTokens())
        .build();
    }
  }
}
