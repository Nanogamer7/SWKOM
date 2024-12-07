package at.fhtw.paperless.ocr.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "documentQueue";

    @Bean
    public Queue documentQueue() {
        return new Queue(QUEUE_NAME, true);
    }
}