package simon.articleservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import simon.articleservice.config.ArticleQueueConfig;
import simon.articleservice.model.Article;
import simon.articleservice.model.Continent;
import simon.articleservice.model.PublishedArticle;

@Component
public class ArticleQueueListener {
    private static final Logger log = LoggerFactory.getLogger(ArticleQueueListener.class);
    private final ArticleService service;

    public ArticleQueueListener(ArticleService service) {
        this.service = service;
    }

    @RabbitListener(queues = ArticleQueueConfig.QUEUE)
    void onArticlePublished(PublishedArticle published) {
        Continent continent = Continent.fromPath(published.continent());
        Article saved = service.create(continent, new Article(published.title(), published.content()));
        log.info("Stored published article {} in {}", saved.getId(), continent);
    }
}
