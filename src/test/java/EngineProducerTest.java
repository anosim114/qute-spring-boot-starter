import configs.TestLocaleConfig;
import io.quarkus.qute.Qute;
import net.snemeis.configurations.EngineProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = { EngineProducer.class, EngineProducerTest.SampleComponent.class })
@Import(TestLocaleConfig.class)
public class EngineProducerTest {

    @Autowired
    ApplicationContext context;
    @Autowired
    private MessageSource messageSource;

    @Value("${spring.messages.basename}")
    String messageBasename;

    @Test
    void Qute_engine_is_being_set() {
        assertNotNull(Qute.engine());
    }

    @Test
    void qute_reners_some_string() {
        String out = Qute.engine().parse("hello from qute :)").render();
        assertEquals("hello from qute :)", out);
    }

    @Test
    void sample_component_is_resolvable() {
        SampleComponent sampleComponent = (SampleComponent) context.getBean("SampleComponent");

        String numResult = sampleComponent.getNumberAsString(42);
        String i18Result = sampleComponent.translate("test.one");

        assertEquals("{42}", numResult);
        assertEquals("this is a test message", i18Result);
    }

    @Test
    void qute_resolves_bean_and_applies_its_method() {
        String out = Qute.engine().parse("{inject:SampleComponent.getNumberAsString(42)}").render();
        assertEquals("{42}", out);
    }

    @Test
    void qute_resolves_bean_and_applies_its_method_with_data() {
        String out = Qute.engine().parse("{cdi:SampleComponent.getNumberAsString(num)}").data("num", 42).render();
        assertEquals("{42}", out);
    }

    @Test
    void qute_translates_messages_via_the_sample_component() {
        String out = Qute.engine().parse("This is the translated message: {cdi:SampleComponent.translate('something')}").render();
        assertEquals("This is the translated message: default message", out);
    }

    @Test
    void qute_resolves_map_to_usable_variables() {
        Model model = new ConcurrentModel();
        model.addAttribute("name", "World");

        String out = Qute.engine()
                .parse("Hello {name}!")
                .render(model.asMap());
        assertEquals("Hello World!", out);

        String out2 = Qute.engine()
                .parse("Hello {name}!")
                .data(model.asMap())
                .render();
        assertEquals("Hello World!", out2);
    }

    @Test
    void qute_resolved_messages_via_msg_namespace_resolver() {
        // prepare
        System.out.println(messageBasename);
        String message = messageSource.getMessage("name", null, "www", Locale.ENGLISH);

        // execute
        var template = Qute.engine().parse("Hello {msg:t('name')}!");
        var out = template.render();

        // check
        assertEquals(out, "Hello World!");
    }

    @Test
    void qute_resolved_messages_with_params_via_msg_namespace_resolver() {
        // prepare

        // execute
        var template = Qute.engine().parse("Hello {msg:t('parameterized', 1, 2)}");
        var out = template.render();

        // check
        assertEquals(out, "Hello One 1 two 2");
    }

    @Test
    void qute_resolves_messages_with_variables_as_attributes() {
        // prepare
        var data = Map.of("v1", 1, "v2", "nitarou");

        // execute
        var template = Qute.engine().parse("Hello {msg:t('parameterized', v1, v2)}");
        var out = template.render(data);

        // check
        assertEquals(out, "Hello One 1 two nitarou");
    }

    @TestComponent(value = "SampleComponent")
    public static class SampleComponent {

        @Autowired
        MessageSource messageSource;

        public String getNumberAsString(int number) {
            return "{%d}".formatted(number);
        }

        public String translate(String key, String... args) {
            return messageSource.getMessage(key, args, "default message", Locale.ENGLISH);
        }
    }
}
