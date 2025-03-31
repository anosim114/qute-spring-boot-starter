import io.quarkus.qute.Qute;
import io.quarkus.qute.TemplateExtension;
import net.snemeis.EngineProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = { EngineProducer.class, TemplateExtensionTest.IntExtension.class })
public class TemplateExtensionTest {
    @Test
    void templateExtensionsAreBeingUsed() {
        Integer number = 1;
        String out = Qute.fmt("{num.doubleIt}", Map.of("num", number));

        assertEquals("2", out);
    }

    @TemplateExtension
    public static class IntExtension {

        public static Integer doubleIt(Integer somNum) {
            return somNum * 2;
        }

        public Integer halfIt(Integer num) {
            return num / 2;
        }
    }
}
