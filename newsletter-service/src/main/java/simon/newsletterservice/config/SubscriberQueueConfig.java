package simon.newsletterservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// The queue is durable: if we are down, subscriber messages wait here until we are back
@Configuration
public class SubscriberQueueConfig {
    public static final String EXCHANGE = "subscribers";
    public static final String QUEUE = "newsletter-service.subscribers";

    @Bean
    FanoutExchange subscriberExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    Queue subscriberQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    Binding subscriberBinding(Queue subscriberQueue, FanoutExchange subscriberExchange) {
        return BindingBuilder.bind(subscriberQueue).to(subscriberExchange);
    }

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
