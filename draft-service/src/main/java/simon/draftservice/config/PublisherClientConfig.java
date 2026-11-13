package simon.draftservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PublisherClientConfig {
    // Built from Spring's RestClient.Builder so the trace id is passed on to the publisher-service
    @Bean
    RestClient publisherRestClient(RestClient.Builder builder, @Value("${publisher.url}") String url) {
        return builder.baseUrl(url).build();
    }
}
