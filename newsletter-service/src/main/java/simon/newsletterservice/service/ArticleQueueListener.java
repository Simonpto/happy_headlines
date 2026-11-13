package simon.newsletterservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import simon.newsletterservice.config.ArticleQueueConfig;
import simon.newsletterservice.model.Article;

@Component
public class ArticleQueueListener {
    private static final Logger log = LoggerFactory.getLogger(ArticleQueueListener.class);
    private final NewsletterService service;

    public ArticleQueueListener(NewsletterService service) {
        this.service = service;
    }

    @RabbitListener(queues = ArticleQueueConfig.QUEUE)
    void onArticlePublished(Article article) {
        log.debug("Received article '{}' from the queue", article.title());
        service.sendBreakingNews(article);
    }
}
