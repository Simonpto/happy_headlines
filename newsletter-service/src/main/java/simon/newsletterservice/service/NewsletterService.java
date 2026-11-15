package simon.newsletterservice.service;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import simon.newsletterservice.client.ArticleClient;
import simon.newsletterservice.model.Article;
import simon.newsletterservice.model.Subscriber;
import simon.newsletterservice.model.SubscriberEvent;
import simon.newsletterservice.repository.SubscriberDatabase;

import java.util.List;

// "Sending" a newsletter is just a log line - there is no real mail server.
// We log subscriber ids, never e-mail addresses.
@Service
@Observed(name = "newsletter.service")
public class NewsletterService {
    private static final Logger log = LoggerFactory.getLogger(NewsletterService.class);
    private final SubscriberDatabase database;
    private final ArticleClient articleClient;

    public NewsletterService(SubscriberDatabase database, ArticleClient articleClient) {
        this.database = database;
        this.articleClient = articleClient;
    }

    // Called for every message on the subscriber queue
    public void updateSubscriber(SubscriberEvent event) {
        if (event.type() == SubscriberEvent.Type.SUBSCRIBED) {
            Subscriber subscriber = new Subscriber(event.email());
            subscriber.setId(event.id());
            database.save(subscriber);
            log.info("Sent welcome mail to new subscriber {}", event.id());
        } else {
            database.delete(event.id());
            log.info("Removed subscriber {} from the newsletter", event.id());
        }
    }

    // Called for every article that comes in on the queue
    public void sendBreakingNews(Article article) {
        List<Subscriber> subscribers = database.findAll();
        for (Subscriber subscriber : subscribers) {
            log.debug("Sending breaking news '{}' to subscriber {}", article.title(), subscriber.getId());
        }
        log.info("Sent breaking news '{}' to {} subscribers", article.title(), subscribers.size());
    }

    // Fetches the 10 newest articles from the article-service and sends them as one newsletter
    public int sendNewsletter(String continent) {
        List<Article> articles = articleClient.findNewest(continent, 10);
        List<Subscriber> subscribers = database.findAll();
        for (Subscriber subscriber : subscribers) {
            log.debug("Sending newsletter with {} articles to subscriber {}", articles.size(), subscriber.getId());
        }
        log.info("Sent {} newsletter with {} articles to {} subscribers", continent, articles.size(), subscribers.size());
        return subscribers.size();
    }
}
