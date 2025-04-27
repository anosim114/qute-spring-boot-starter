package io.quarkus.qute;

import io.quarkus.qute.CacheSectionHelper.Cache;
import lombok.extern.slf4j.Slf4j;
import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = { EngineProducer.class, CacheSectionTest.SectionHelperConfig.class })
@ExtendWith(OutputCaptureExtension.class)
public class CacheSectionTest {

  @Autowired
  public ConcurrentMap cacheMap;

  Engine engine;

  @BeforeEach
  void initEngine() {
    this.engine = Qute.engine();
    this.cacheMap.clear();
  }

  @Test
  public void testCached() {
    Template template = engine.parse("{#cached}{counter.val}{/cached}", null, "foo.html");
    Counter counter = new Counter();

    assertEquals("1", template.data("counter", counter).render());
    assertEquals(1, cacheMap.size());
    // {counter.val} was cached
    assertEquals("1", template.data("counter", counter).render());
    // Invalidate all cache entries
    cacheMap.clear();
    assertEquals("2", template.data("counter", counter).render());
    assertEquals("2", template.data("counter", counter).render());
    assertEquals(1, cacheMap.size());
    assertEquals("foo.html:1:1_", cacheMap.keySet().iterator().next());
  }

  @Test
  public void testCachedWithKey() {
    Template template = engine.parse("{#cached key=myKey}{counter.val}{/cached}");
    Counter counter = new Counter();

    assertEquals("1", template.data("counter", counter, "myKey", "foo").render());
    assertEquals("2", template.data("counter", counter, "myKey", "bar").render());
    assertEquals("1", template.data("counter", counter, "myKey", "foo").render());
    assertEquals("2", template.data("counter", counter, "myKey", "bar").render());
    assertEquals("3", template.data("counter", counter, "myKey", "baz").render());
    assertEquals(3, cacheMap.size());
  }

  // TODO: tests seem to work correctly, just the failure is registered globally and not within the check
  @Test
  public void testCachedWithFailure(CapturedOutput out) {
    Template template = engine.parse("{#cached}{counter.getVal(fail)}{/cached}");
    Counter counter = new Counter();

    assertThrows(java.lang.IllegalStateException.class, () -> template.data("counter", counter, "fail", true).render());
    // The failure is cached
    assertThrows(java.lang.IllegalStateException.class, () -> template.data("counter", counter, "fail", false).render());
    assertEquals(1, cacheMap.size());
    // Invalidate all cache entries
    cacheMap.clear();
    assertEquals("1", template.data("counter", counter, "fail", false).render());
    // The success is cached
    assertEquals("1", template.data("counter", counter, "fail", true).render());
    assertEquals(1, cacheMap.size());
  }

  public static class Counter {

    private final AtomicInteger val = new AtomicInteger();

    public int getVal() {
      return val.incrementAndGet();
    }

    public int getVal(boolean failure) {
      if (failure) {
        throw new IllegalStateException();
      }
      return val.incrementAndGet();
    }

  }

  @TestConfiguration
  static class SectionHelperConfig {
    Logger logger = LoggerFactory.getLogger(SectionHelperConfig.class);
    @Bean
    ConcurrentMap<String, CompletionStage<ResultNode>> cacheMap() {
      return new ConcurrentHashMap<>();
    }

    @Bean
    public SectionHelperFactory someSectionHelper (ConcurrentMap<String, CompletionStage<ResultNode>> cacheMap) {
      return new CacheSectionHelper.Factory(new Cache() {
        @Override
        public CompletionStage<ResultNode> getValue(
          String key,
          Function<String, CompletionStage<ResultNode>> loader
        ) {

          logger.info("cache was hit with key: {}", key);
          return cacheMap.computeIfAbsent(key, loader::apply);
        }
      });
    }
  }
}
