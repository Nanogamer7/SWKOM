package at.fhtw.paperless.ocr;

import at.fhtw.paperless.ocr.config.RabbitMQConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@RequiredArgsConstructor
public class QueueListener {

    private final MinioClient minioClient;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receive(String filename) {
        System.out.println(filename);

        try (InputStream stream = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket("paperless")
                .object(filename)
                .build())) {

            // put code here

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
