package io.quarkus.qute;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(OutputCaptureExtension.class)
public class NodeResolveTraceLoggingTest {

  static void setLevel(Logger logger, Level level) {
    logger.setLevel(level);
    for (Handler handler : logger.getHandlers()) {
      handler.setLevel(level);
    }
  }

  @BeforeAll
  static void initLogger() {
    // https://stackoverflow.com/questions/61792208/how-to-set-logging-at-runtime-in-org-jboss-logging
    // TODO: document that this might be a problem and had to be done manually
    System.setProperty("org.jboss.logging.provider", "slf4j");
  }

  @Test
  public void testTraceLog(CapturedOutput out) {
    Logger root = Logger.getLogger("");
    TestHandler handler = new TestHandler();
    root.addHandler(handler);
    Level previousLevel = root.getLevel();
    setLevel(root, Level.FINEST);

    Engine engine = Engine.builder().addDefaults().timeout(200).build();

    assertEquals("Hello world!", engine.parse("Hello {name}!", null, "hello").data("name", "world").render());

    assertTrue(out.getOut().contains("Resolve {name} started: template [hello] line 1"));
    assertTrue(out.getOut().contains("Resolve {name} completed: template [hello] line 1"));

    try {
      engine.parse("{foo}", null, "foo").data("foo", new CompletableFuture<>()).render();
      fail();
    } catch (TemplateException expected) {
    }
    assertTrue(out.getOut().contains("Resolve {foo} started: template [foo] line 1"));

    assertEquals("Hello world!", engine.parse("Hello {#if true}world{/if}!", null, "helloIf").render());
    assertTrue(out.getOut().contains("Resolve {#if} started: template [helloIf] line 1"));
    assertTrue(out.getOut().contains("Resolve {#if} completed: template [helloIf] line 1"));

    try {
      engine.parse("{#if foo}{/if}", null, "fooIf").data("foo", new CompletableFuture<>()).render();
      fail();
    } catch (TemplateException expected) {
    }
    assertTrue(out.getOut().contains("Resolve {#if} started: template [fooIf] line 1"));

    try {
      engine.parse("{#if true}{foo}{/if}", null, "fooIf2").data("foo", new CompletableFuture<>()).render();
      fail();
    } catch (TemplateException expected) {
    }
    assertTrue(out.getOut().contains("Resolve {#if} started: template [fooIf2] line 1"));
    assertTrue(out.getOut().contains("Resolve {foo} started: template [fooIf2] line 1"));

    setLevel(root, previousLevel);
  }

  static class TestHandler extends Handler {

    final List<LogRecord> records = new CopyOnWriteArrayList<>();

    @Override
    public void publish(LogRecord record) {
      if (record.getLoggerName().equals("io.quarkus.qute.nodeResolve")) {
        records.add(record);
      }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

  }
}
