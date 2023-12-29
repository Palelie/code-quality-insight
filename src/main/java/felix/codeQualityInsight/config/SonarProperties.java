package felix.codeQualityInsight.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sonarqube")
@Data
public class SonarProperties {

    private String url;
    private String token;
    private String globalToken;

}
