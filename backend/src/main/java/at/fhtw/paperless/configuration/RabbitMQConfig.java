package at.fhtw.paperless.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;


@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue documentQueue() {
        return new Queue("documentQueue", true);
    }
}