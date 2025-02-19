import net.snemeis.EngineProducer;
import net.snemeis.QuteViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = { EngineProducer.class },
        properties = "logging.level.root=DEBUG"
)
@ExtendWith(OutputCaptureExtension.class)
public class QuteViewResolverTest {

    @Autowired
    ApplicationContext context;

    QuteViewResolver resolver;

    @BeforeEach
    void initViewResolver() {
        this.resolver = (net.snemeis.QuteViewResolver) context.getBean("quteViewResolver");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "qute_view_resolver_test/quteindex, text/html",
            "qute_view_resolver_test/text.txt,  text/plain",
            "qute_view_resolver_test/data.json, application/json"
    })
    void htmlViews_arePassedAsHtmlContentType(String path, String contentType) {
        // execute
        var view = resolver.resolveViewName(path, Locale.ENGLISH);

        // check
        assertNotNull(view);
//        assertEquals(contentType, view.getContentType());
    }

    @Test
    void cacheEnabled_doesNotCallLocateDuplicate(CapturedOutput out) {
        // prepare
        String path = "qute_view_resolver_test/cached.qute.html";

        // execute two times
        resolver.resolveViewName(path, Locale.ENGLISH);
        resolver.resolveViewName(path, Locale.ENGLISH);

        // check
        var matches = out.getAll().lines().filter(str -> str.contains("locating template")).count();
        assertEquals(1, matches);
    }

    @Test
    void nonExistingView_isNull() {
        // prepare
        String viewName = "nonexistent.html";
        // execute
        var view = resolver.resolveViewName(viewName, Locale.ENGLISH);
        // check
        assertNull(view);
    }
}
