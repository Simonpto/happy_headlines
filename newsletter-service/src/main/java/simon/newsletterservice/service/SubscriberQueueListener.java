package simon.newsletterservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import simon.newsletterservice.config.SubscriberQueueConfig;
import simon.newsletterservice.model.SubscriberEvent;

@Component
public class SubscriberQueueListener {
    private final NewsletterService service;

    public SubscriberQueueListener(NewsletterService service) {
        this.service = service;
    }

    @RabbitListener(queues = SubscriberQueueConfig.QUEUE)
    void onSubscriberChanged(SubscriberEvent event) {
        service.updateSubscriber(event);
    }
}
