import io.quarkus.qute.Qute;
import io.quarkus.qute.TemplateExtension;
import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {EngineProducer.class, TemplateExtensionTest.SomeExtensions.class})
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

  @Test
  void stringCapitalizeExtension() {
    Integer number = 1;
    String out = Qute.fmt("{str.capitalize}", Map.of("str", "abc - the quteness likes a bee"));

    assertEquals("ABC - THE QUTENESS LIKES A BEE", out);
  }

  @TemplateExtension
  public static class SomeExtensions {

    public static Integer doubleIt(Integer somNum) {
      return somNum * 2;
    }

    public static Integer multiplyBy(Integer num, Integer multiplicationFactor) {
      return num * multiplicationFactor;
    }

    public static Integer addAll(Integer num, Integer one, Integer two, Integer three) {
      return num + one + two + three;
    }

    public static String capitalize(String str) {
      return str.toUpperCase(Locale.getDefault());
    }
  }
}
