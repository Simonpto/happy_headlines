package simon.newsletterservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ArticleClientConfig {
    // Built from Spring's RestClient.Builder so the trace id is passed on to the article-service
    @Bean
    RestClient articleRestClient(RestClient.Builder builder, @Value("${articles.url}") String url) {
        return builder.baseUrl(url).build();
    }
}
