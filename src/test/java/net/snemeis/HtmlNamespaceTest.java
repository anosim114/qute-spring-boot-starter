package net.snemeis;

import io.quarkus.qute.Qute;
import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {EngineProducer.class})
class HtmlNamespaceTest {

  @Test
  void htmlAttrs() {
    String output = Qute.fmt("{html:attr('id', theId)}")
      .data("theId", "some-id")
      .render();

    assertEquals("id=\"some-id\"", output);
  }

  @Test
  void htmlAttrs2() {
    String output = Qute.fmt("{html:attr(theKey, theValue)}")
      .data("theKey","key")
      .data("theValue", "value")
      .render();

    assertEquals("key=\"value\"", output);
  }

  @Test
  void htmlAttrs_withNull() {
    String output;

    output = Qute.fmt("{html:attr('someKey', nullValue)}")
      .data("nullValue", null)
      .render();
    assertEquals("", output);

    output = Qute.fmt("{html:attr(nullValue, 'some-value')}")
      .data("nullValue", null)
      .render();
    assertEquals("", output);
  }

  @Test
  void htmlAttrs_classes() {
    String output = Qute.fmt("{html:class(theValue, 'some other values')}")
      .data("theValue", "value")
      .render();

    assertEquals("class=\"value some other values\"", output);
  }

  @Test
  void htmlAttrs_classesEmpty() {
    String output = Qute.fmt("{html:class(theValue)}")
      .data("theValue", null)
      .render();

    assertEquals("", output);
  }

  @Test
  void htmlAttrs_classesWithNullValues() {
    String output = Qute.fmt("{html:class(theValue, nullValue, 'without non value class')}")
      .data("theValue", "value")
      .data("nullValue", null)
      .render();

    assertEquals("class=\"value without non value class\"", output);
  }
}
