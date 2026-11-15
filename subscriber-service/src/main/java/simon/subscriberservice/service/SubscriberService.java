package simon.subscriberservice.service;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simon.subscriberservice.config.SubscriberQueueConfig;
import simon.subscriberservice.model.Subscriber;
import simon.subscriberservice.model.SubscriberEvent;
import simon.subscriberservice.repository.SubscriberDatabase;

import java.util.List;

// The newsletter-service never calls us - it only hears about subscribers through the queue.
// That way either service can be down without taking the other one with it.
@Service
@Observed(name = "subscriber.service")
public class SubscriberService {
    private static final Logger log = LoggerFactory.getLogger(SubscriberService.class);
    private final SubscriberDatabase database;
    private final RabbitTemplate rabbitTemplate;
    private final ReleaseToggleService toggles;

    public SubscriberService(SubscriberDatabase database, RabbitTemplate rabbitTemplate, ReleaseToggleService toggles) {
        this.database = database;
        this.rabbitTemplate = rabbitTemplate;
        this.toggles = toggles;
    }

    public List<Subscriber> findAll(int page, int pageSize) {
        toggles.requireEnabled(ReleaseToggleService.SUBSCRIBER_SERVICE);
        return database.findAll(page, pageSize);
    }

    public Subscriber subscribe(Subscriber subscriber) {
        toggles.requireEnabled(ReleaseToggleService.SUBSCRIBER_SERVICE);
        Subscriber saved = database.insert(subscriber);
        send(new SubscriberEvent(SubscriberEvent.Type.SUBSCRIBED, saved.getId(), saved.getEmail()));
        log.info("New subscriber {}", saved.getId());
        return saved;
    }

    public void unsubscribe(Long id) {
        toggles.requireEnabled(ReleaseToggleService.SUBSCRIBER_SERVICE);
        Subscriber subscriber = database.findById(id).orElseThrow(() -> {
            log.warn("Subscriber {} not found", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscriber " + id + " not found");
        });
        database.delete(id);
        send(new SubscriberEvent(SubscriberEvent.Type.UNSUBSCRIBED, subscriber.getId(), subscriber.getEmail()));
        log.info("Subscriber {} unsubscribed", id);
    }

    private void send(SubscriberEvent event) {
        rabbitTemplate.convertAndSend(SubscriberQueueConfig.EXCHANGE, "", event);
    }
}
