package at.fhtw.paperless.queuelistener;


import at.fhtw.paperless.configuration.RabbitMQConfig;
import at.fhtw.paperless.dal.repositories.DocumentMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QueueListener {
    final DocumentMetadataRepository documentMetadataRepository;


    @RabbitListener(queues = RabbitMQConfig.RETURN_QUEUE)
    @Transactional
    public void receiveReturnMessage(String message) {
        System.out.println("QueueListener received message: " + message);

        int rowsAffected = documentMetadataRepository.updateScannedByFileName(message, true);

        System.out.println("Rows affected: " + rowsAffected);
    }
}
