package simon.newsletterservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArticleQueueConfig {
    public static final String EXCHANGE = "articles";
    public static final String QUEUE = "newsletter-service.articles";

    @Bean
    FanoutExchange articleExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    Queue articleQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    Binding articleBinding(Queue articleQueue, FanoutExchange articleExchange) {
        return BindingBuilder.bind(articleQueue).to(articleExchange);
    }

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
