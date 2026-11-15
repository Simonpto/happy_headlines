package simon.subscriberservice.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriberQueueConfig {
    public static final String EXCHANGE = "subscribers";

    @Bean
    FanoutExchange subscriberExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
