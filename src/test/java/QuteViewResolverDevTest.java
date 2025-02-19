import net.snemeis.EngineProducer;
import net.snemeis.QuteViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = { EngineProducer.class },
        properties = "logging.level.root=DEBUG"
)
@ExtendWith(OutputCaptureExtension.class)
@ActiveProfiles("dev")
public class QuteViewResolverDevTest {

    @Autowired
    ApplicationContext context;

    QuteViewResolver resolver;

    @BeforeEach
    void initViewResolver() {
        this.resolver = (QuteViewResolver) context.getBean("quteViewResolver");
    }

    @Test
    void devMode_findsByDirectPath() {
        // prepare
        String path = "qute_view_resolver_test/quteindex";
        // execute
        var view = resolver.resolveViewName(path, Locale.ENGLISH);
        // check
        assertNotNull(view);
    }

    @Test
    void cacheEnabled_doesNotCallLocateDuplicate(CapturedOutput out) {
        // prepare
        String path = "qute_view_resolver_test/not-cached.qute.html";

        // execute two times
        resolver.resolveViewName(path, Locale.ENGLISH);
        resolver.resolveViewName(path, Locale.ENGLISH);

        // check
        var matches = out.getAll().lines().filter(str -> str.contains("locating template")).count();
        assertEquals(2, matches);
    }
}
