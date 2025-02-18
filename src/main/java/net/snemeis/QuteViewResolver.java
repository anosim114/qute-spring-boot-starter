package net.snemeis;

import io.quarkus.qute.Qute;
import io.quarkus.qute.Variant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class QuteViewResolver implements ViewResolver {

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

        var contentType = template.getVariant().map(Variant::getContentType).orElse("text/plain");

        return new View() {
            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public void render(Map<String, ?> model, @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws Exception {
                // TODO: add RequestContext attribute to data
                var html = template.render(model);

                response.setContentType(getContentType());
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(html);
            }
        };
    }
}
