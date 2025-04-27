package io.quarkus.qute;

import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {EngineProducer.class})
public class CollectionResolverTest {

  Engine engine;

  @BeforeEach
  void initEngine() {
    this.engine = Qute.engine();
  }

  @Test
  public void testResolver() {
    List<String> list = new ArrayList<>();
    list.add("Lu");

    assertEquals("1,false,true",
      engine.parse("{this.size},{this.isEmpty},{this.contains('Lu')}").render(list));
  }

  @Test
  public void testListTake() {
    List<String> list = new ArrayList<>();
    list.add("Lu");
    list.add("Roman");
    list.add("Matej");

    assertEquals("Lu,",
      engine.parse("{#each list.take(1)}{it},{/each}").data("list", list).render());
    assertEquals("Roman,Matej,",
      engine.parse("{#each list.takeLast(2)}{it},{/each}").data("list", list).render());
    assertThatExceptionOfType(IndexOutOfBoundsException.class)
      .isThrownBy(() -> engine.parse("{list.take(12).size}").data("list", list).render());
    assertThatExceptionOfType(IndexOutOfBoundsException.class)
      .isThrownBy(() -> engine.parse("{list.take(-1).size}").data("list", list).render());
  }

}
