package simon.newsletterservice.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import simon.newsletterservice.model.Article;

import java.util.List;

@Component
public class ArticleClient {
    private final RestClient articleRestClient;

    public ArticleClient(RestClient articleRestClient) {
        this.articleRestClient = articleRestClient;
    }

    public List<Article> findAll(String continent) {
        return articleRestClient.get()
                .uri("/articles/{continent}", continent)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
