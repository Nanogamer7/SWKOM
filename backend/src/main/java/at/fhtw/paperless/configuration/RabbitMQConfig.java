package at.fhtw.paperless.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;


@Configuration
public class RabbitMQConfig {

    public static final String RETURN_QUEUE = "returnQueue";

    @Bean
    public Queue documentQueue() {
        return new Queue("documentQueue", true);
    }

    @Bean
    public Queue returnQueue() {return new Queue(RETURN_QUEUE, true);}
}