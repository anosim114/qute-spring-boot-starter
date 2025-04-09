package net.snemeis.configurations;

import lombok.AllArgsConstructor;
import net.snemeis.QuteProperties;
import net.snemeis.QuteViewResolver;
import net.snemeis.TemplatePostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;

import java.util.List;

@AllArgsConstructor
@AutoConfiguration(after = WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(QuteProperties.class)
public class QuteViewResolverConfiguration {

  private final QuteProperties config;

  @Bean
  ViewResolver quteViewResolver(List<TemplatePostProcessor> postProcessors) {
    System.out.println("initializing the qute view resolver");
    return new QuteViewResolver(config.cachingEnabled, postProcessors);
  }
}
