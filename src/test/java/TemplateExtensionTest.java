import io.quarkus.qute.Qute;
import io.quarkus.qute.TemplateExtension;
import net.snemeis.EngineProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {EngineProducer.class, TemplateExtensionTest.IntExtension.class})
public class TemplateExtensionTest {
  @Test
  void singleArgExtension() {
    Integer number = 1;
    String out = Qute.fmt("{num.doubleIt}", Map.of("num", number));

    assertEquals("2", out);
  }

  @Test
  void twoArgExtension() {
    Integer number = 1;
    String out = Qute.fmt("{num.multiplyBy(3)}", Map.of("num", number));

    assertEquals("3", out);
  }

  @Test
  void multiArgExtension() {
    Integer number = 1;
    String out = Qute.fmt("{num.addAll(1, 2, 3)}", Map.of("num", number));

    assertEquals("7", out);
  }

  @TemplateExtension
  public static class IntExtension {

    public static Integer doubleIt(Integer somNum) {
      return somNum * 2;
    }

    public static Integer multiplyBy(Integer num, Integer multiplicationFactor) {
      return num * multiplicationFactor;
    }

    public static Integer addAll(Integer num, Integer one, Integer two, Integer three) {
      return num + one + two + three;
    }
  }
}
