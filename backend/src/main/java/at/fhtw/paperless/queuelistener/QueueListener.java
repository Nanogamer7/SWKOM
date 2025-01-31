package at.fhtw.paperless.queuelistener;


import at.fhtw.paperless.configuration.RabbitMQConfig;
import at.fhtw.paperless.dal.repositories.DocumentMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log
public class QueueListener {
    final DocumentMetadataRepository documentMetadataRepository;

    @RabbitListener(queues = RabbitMQConfig.RETURN_QUEUE)
    @Transactional
    public void receiveReturnMessage(String message) {
        log.info("Received message from queue: " + message);

        documentMetadataRepository.updateScannedByFileName(message, true);
    }
}
