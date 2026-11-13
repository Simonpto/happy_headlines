package simon.articleservice.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import simon.articleservice.model.Continent;

import java.util.EnumMap;
import java.util.Map;

@Component
@EnableConfigurationProperties(ArticleDatabaseProperties.class)
public class ArticleDatabase {
    private final Map<Continent, JdbcClient> databases = new EnumMap<>(Continent.class);

    ArticleDatabase(ArticleDatabaseProperties properties) {
        for (Continent continent : Continent.values()) {
            String url = properties.databases().get(continent);
            if (url == null) {
                throw new IllegalStateException("Missing database URL for " + continent + " in application.yaml");
            }
            HikariDataSource database = new HikariDataSource();
            database.setPoolName("articles-" + continent);
            database.setJdbcUrl(url);
            database.setUsername(properties.username());
            database.setPassword(properties.password());

            new ResourceDatabasePopulator(new ClassPathResource("schema/article.sql")).execute(database);
            databases.put(continent, JdbcClient.create(database));
        }
    }

    public JdbcClient get(Continent continent) {
        return databases.get(continent);
    }
}
