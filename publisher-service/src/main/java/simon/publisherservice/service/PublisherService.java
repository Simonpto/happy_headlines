package simon.publisherservice.service;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import simon.publisherservice.config.ArticleQueueConfig;
import simon.publisherservice.model.Article;

@Service
@Observed(name = "publisher.service")
public class PublisherService {
    private static final Logger log = LoggerFactory.getLogger(PublisherService.class);
    private final RabbitTemplate rabbitTemplate;

    public PublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Article article) {
        rabbitTemplate.convertAndSend(ArticleQueueConfig.EXCHANGE, "", article);
        log.info("Put article '{}' for {} on the article queue", article.title(), article.continent());
    }
}
