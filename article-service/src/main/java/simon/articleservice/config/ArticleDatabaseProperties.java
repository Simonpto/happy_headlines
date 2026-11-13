package simon.articleservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import simon.articleservice.model.Continent;

import java.util.Map;

@ConfigurationProperties("articles")
public record ArticleDatabaseProperties(Map<Continent, String> databases, String username, String password) {
}
