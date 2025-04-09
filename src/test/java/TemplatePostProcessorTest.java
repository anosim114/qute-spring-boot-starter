import jakarta.servlet.http.HttpServletRequest;
import net.snemeis.configurations.EngineProducer;
import net.snemeis.QuteViewResolver;
import net.snemeis.TemplatePostProcessor;
import net.snemeis.configurations.QuteViewResolverConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
  EngineProducer.class,
  QuteViewResolverConfiguration.class,
  TemplatePostProcessorTest.VersionTransformer.class,
  TemplatePostProcessorTest.CapitalizeTransformer.class
})
public class TemplatePostProcessorTest {

  @Autowired
  QuteViewResolver resolver;

  @Test
  void stringReplacePostProcessor() throws Exception {
    // prepare
    var mockRequest = new MockHttpServletRequest();
    var mockResponse = new MockHttpServletResponse();
    var view = resolver.resolveViewName("template_post_processor/version_replace", Locale.getDefault());

    // execute
    view.render(Map.of(), mockRequest, mockResponse);

    // check
    assertEquals("<p>This template is version: 1.0.0</p>\n", mockResponse.getContentAsString());
  }

  @Test
  void appliesOnlyWhenApplicable() throws Exception {
    // prepare
    var mockRequest = new MockHttpServletRequest();
    mockRequest.setAttribute("capitalize", true);
    var mockResponse = new MockHttpServletResponse();
    var view = resolver.resolveViewName("template_post_processor/version_replace", Locale.getDefault());

    // execute
    view.render(Map.of(), mockRequest, mockResponse);

    // check
    assertEquals("<P>THIS TEMPLATE IS VERSION: 1.0.0</P>\n", mockResponse.getContentAsString());
  }

  @TestComponent
  public static class VersionTransformer implements TemplatePostProcessor {

    @Override
    public String process(String renderedTemplate) {
      return renderedTemplate.replaceAll("VERSION", "1.0.0");
    }
  }

  @TestComponent
  public static class CapitalizeTransformer implements TemplatePostProcessor {
    @Override
    public String process(String renderedTemplate) {
      return renderedTemplate.toUpperCase();
    }

    @Override
    public Boolean appliesTo(HttpServletRequest request) {
      return Objects.nonNull(request.getAttribute("capitalize"));
    }
  }
}
