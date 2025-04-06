package net.snemeis;

import jakarta.servlet.http.HttpServletRequest;

public interface TemplatePostProcessor {
  String process(String renderedTemplate);

  default Boolean appliesTo(HttpServletRequest request) {
    return true;
  }
}
