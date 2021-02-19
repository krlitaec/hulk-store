package ec.com.todo1.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Hulk Store.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
}
