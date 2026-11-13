package simon.publisherservice.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArticleQueueConfig {
    // Fanout: every service with a queue bound to this exchange gets its own copy of each article
    public static final String EXCHANGE = "articles";

    @Bean
    FanoutExchange articleExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
