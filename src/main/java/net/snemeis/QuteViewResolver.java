package net.snemeis;

import io.quarkus.qute.Qute;
import io.quarkus.qute.Template;
import io.quarkus.qute.Variant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class QuteViewResolver implements ViewResolver, Ordered {

  @Getter
  private final int order = Ordered.HIGHEST_PRECEDENCE;

  private final Boolean cachingEnabled;
  private final List<TemplatePostProcessor> postProcessors;

  @Override
  public View resolveViewName(@NonNull String viewName, @NonNull Locale locale) {
    log.debug("resolving view {}", viewName);

    if (!this.cachingEnabled) {
      Qute.engine().clearTemplates();
    }

    var template = Qute.engine().getTemplate(viewName);
    if (template == null) {
      return null;
    }

    return new QuteView(template, postProcessors);
  }

  static class QuteView implements View {
    private final Template template;
    private final List<TemplatePostProcessor> postProcessors;
    private final String contentType;

    public QuteView(Template template, List<TemplatePostProcessor> postProcessors) {
      this.template = template;
      this.postProcessors = postProcessors;
      this.contentType = template.getVariant().map(Variant::getContentType).orElse("text/plain");
    }

    @Override
    public String getContentType() {
      return contentType;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
      var html = template.render(model);

      for (TemplatePostProcessor processor : postProcessors) {
        if (processor.appliesTo(request)) {
          html = processor.process(html);
        }
      }

      response.setContentType(contentType);
      response.setCharacterEncoding(StandardCharsets.UTF_8.name());
      response.getWriter().write(html);
    }
  }
}
