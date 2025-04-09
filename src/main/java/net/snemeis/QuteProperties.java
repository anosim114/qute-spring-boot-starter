package net.snemeis;

import io.quarkus.qute.TemplateException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Data
@ConfigurationProperties(prefix = "spring.qute")
public class QuteProperties {

  /*
   * General Configs
   */
  public List<String> suffixes = List.of("", ".qute.html");

  public String prefix = "/templates/";

  public String devPrefix = System.getProperty("user.dir") + "/src/main/resources/templates/";

  public Boolean devMode = false;

//    public Map<String, String> contentTypes = Map.of("qute.html", "text/html");

  public List<String> typeCheckExcludes = List.of();

  public Pattern templatePathExclude = Pattern.compile("^\\..*|.*\\/\\..*$");

  public String iterationMetadataPrefix = "<alias_>";

  public List<String> escapeContentTypes = List.of("text/html", "text/xml", "application/xml", "application/xhtml+xml");

  public Charset defaultCharset = StandardCharsets.UTF_8;

  public Boolean cachingEnabled = true;
  /*
   * Runtime Configs
   */
  public PropertyNotFoundStrategy propertyNotFoundStrategy = PropertyNotFoundStrategy.DEFAULT;
  public boolean removeStandaloneLines = true;

  // List<String> templatePaths = List.of();
  public boolean strictRendering = true;
  public long timeout = 10000;
  public boolean useAsyncTimeout = true;
  /*
   * Temporary Extra Configs
   */
  public List<String> resolverClasses = List.of(); // TODO: is this relevant?
  public Locale defaultLocale = Locale.ENGLISH; // TODO: is this relevant?

  public enum PropertyNotFoundStrategy {
    /**
     * Output the {@code NOT_FOUND} constant.
     */
    DEFAULT,
    /**
     * No operation - no output.
     */
    NOOP,

    /**
     * Throw a {@link TemplateException}.
     */
    THROW_EXCEPTION,
    /**
     * Output the original expression string, e.g. <code>{foo.name}</code>.
     */
    OUTPUT_ORIGINAL
  }
}
