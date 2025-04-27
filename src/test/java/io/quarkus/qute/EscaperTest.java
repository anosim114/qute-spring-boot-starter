package io.quarkus.qute;

import io.quarkus.qute.TemplateNode.Origin;
import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {EngineProducer.class, EscaperTest.EscaperConfiguration.class})
public class EscaperTest {

  Engine engine;

  @BeforeEach
  void initEngine() {
    this.engine = Qute.engine();
  }

  @Test
  public void testEscaping() throws IOException {
    Escaper escaper = Escaper.builder().add('a', "aaa").build();
    assertEquals("aaa", escaper.escape("a"));
    assertEquals("b", escaper.escape("b"));

    Escaper html = Escaper.builder().add('"', "&quot;").add('\'', "&#39;")
      .add('&', "&amp;").add('<', "&lt;").add('>', "&gt;").build();
    assertEquals("&lt;strong&gt;Čolek&lt;/strong&gt;", html.escape("<strong>Čolek</strong>"));
    assertEquals("&lt;a&gt;&amp;link&quot;&#39;&lt;/a&gt;", html.escape("<a>&link\"'</a>"));
  }

  @Test
  public void testRawStringResolver() {
    assertEquals("HAM HaM", engine.parse("{foo} {foo.raw}").data("foo", "HaM").render());
  }

  @TestConfiguration
  static class EscaperConfiguration {

    @Bean
    public ResultMapper aaaResultMapper() {
      Escaper escaper = Escaper.builder().add('a', "A").build();

      return new ResultMapper() {
        @Override
        public boolean appliesTo(Origin origin, Object result) {
          return result instanceof String;
        }

        @Override
        public String map(Object result, Expression expression) {
          return escaper.escape(result.toString());
        }
      };
    }
  }

}
