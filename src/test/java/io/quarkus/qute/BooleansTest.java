package io.quarkus.qute;

import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {EngineProducer.class})
public class BooleansTest {
  Engine engine;

  @BeforeEach
  void initEngine() {
    this.engine = Qute.engine();
  }

  @Test
  public void testIsFalsy() {
    assertTrue(Booleans.isFalsy(null));
    assertTrue(Booleans.isFalsy(false));
    assertTrue(Booleans.isFalsy(""));
    assertTrue(Booleans.isFalsy(new StringBuffer()));
    assertTrue(Booleans.isFalsy(0f));
    assertTrue(Booleans.isFalsy(0));
    assertTrue(Booleans.isFalsy(0.0));
    assertTrue(Booleans.isFalsy(new BigDecimal("0.0")));
    assertTrue(Booleans.isFalsy(new BigInteger("0")));
    assertTrue(Booleans.isFalsy(new ArrayList<>()));
    assertTrue(Booleans.isFalsy(new HashSet<>()));
    assertTrue(Booleans.isFalsy(new HashMap<>()));
    assertTrue(Booleans.isFalsy(new String[0]));
    assertTrue(Booleans.isFalsy(new AtomicLong(0)));
    assertTrue(Booleans.isFalsy(new AtomicBoolean(false)));
//        assertTrue(Booleans.isFalsy(Optional.empty()));
//        assertTrue(Booleans.isFalsy(OptionalInt.empty()));
//        assertTrue(Booleans.isFalsy(OptionalLong.empty()));
//        assertTrue(Booleans.isFalsy(OptionalDouble.empty()));
    // truthy values
    assertFalse(Booleans.isFalsy(new Object()));
    assertFalse(Booleans.isFalsy(true));
    assertFalse(Booleans.isFalsy("foo"));
    assertFalse(Booleans.isFalsy(new StringBuffer().append("foo")));
    assertFalse(Booleans.isFalsy(1f));
    assertFalse(Booleans.isFalsy(4));
    assertFalse(Booleans.isFalsy(0.2));
    assertFalse(Booleans.isFalsy(new BigDecimal("0.1")));
    assertFalse(Booleans.isFalsy(new BigInteger("10")));
    assertFalse(Booleans.isFalsy(Collections.singletonList("foo")));
    assertFalse(Booleans.isFalsy(Collections.singletonMap("foo", "bar")));
    assertFalse(Booleans.isFalsy(new String[]{"foo"}));
    assertFalse(Booleans.isFalsy(new AtomicLong(10)));
    assertFalse(Booleans.isFalsy(new AtomicBoolean(true)));
    assertFalse(Booleans.isFalsy(Optional.of("foo")));
    assertFalse(Booleans.isFalsy(OptionalInt.of(1)));
    assertFalse(Booleans.isFalsy(OptionalLong.of(10l)));
    assertFalse(Booleans.isFalsy(OptionalDouble.of(1.0)));
  }

  @Test
  public void testLogicalOperators() {
    assertEquals("true", engine.parse("{foo && bar}").data("foo", true).data("bar", 1).render());
    assertEquals("true", engine.parse("{foo && bar && baz}").data("foo", true).data("bar", 1).data("baz", true).render());
    assertEquals("false",
      engine.parse("{foo && bar && baz}").data("foo", true).data("bar", 1).data("baz", false).render());
    assertEquals("true", engine.parse("{foo || bar}").data("foo", true).data("bar", 0).render());
    assertEquals("false", engine.parse("{foo || bar || baz}").data("foo", false).data("bar", 0)
      .data("baz", Collections.emptyList()).render());
  }
}
