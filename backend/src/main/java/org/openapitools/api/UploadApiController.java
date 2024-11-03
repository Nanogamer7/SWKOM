package org.openapitools.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("${openapi.paperless.base-path:}")
public class UploadApiController implements UploadApi {

    private static final Logger logger = LoggerFactory.getLogger(UploadApiController.class);

    private final NativeWebRequest request;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UploadApiController(NativeWebRequest request, RabbitTemplate rabbitTemplate) {
        this.request = request;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> uploadPost(MultipartFile file, String description) {
        try {
            // Logge das Hochladen des Dokuments
            logger.info("Uploading document: {}", file.getOriginalFilename());

            // Zusätzliche Protokollierung vor dem Senden der Nachricht
            logger.info("Attempting to send message to RabbitMQ queue 'documentQueue'");

            // Sende die Nachricht an RabbitMQ
            rabbitTemplate.convertAndSend("documentQueue", "Document uploaded: " + file.getOriginalFilename());
            logger.info("Message sent to RabbitMQ for document: {}", file.getOriginalFilename());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error("Error uploading document to RabbitMQ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}