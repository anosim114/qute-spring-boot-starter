package net.snemeis;

import io.quarkus.qute.Qute;
import io.quarkus.qute.Template;
import io.quarkus.qute.Variant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class QuteViewResolver implements ViewResolver, Ordered {

    @Getter
    private final int order = Ordered.HIGHEST_PRECEDENCE;

    Boolean cachingEnabled;

    public QuteViewResolver(Boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
    }

    @Override
    public View resolveViewName(@NonNull String viewName, @NonNull Locale locale) {
        if (!this.cachingEnabled) {
            Qute.engine().clearTemplates();
        }

        var template = Qute.engine().getTemplate(viewName);
        if (template == null) {
            return null;
        }

        return new QuteView(template);
    }

    static class QuteView implements View {
        private final Template template;
        private final String contentType;

        public QuteView(Template template) {
            this.template = template;
            this.contentType = template.getVariant().map(Variant::getContentType).orElse("text/plain");
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
            var html = template.render(model);

            response.setContentType(contentType);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(html);
        }
    }
}
