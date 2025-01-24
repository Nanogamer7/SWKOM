package at.fhtw.paperless.api;

import at.fhtw.paperless.dal.models.DocumentMetadata;
import at.fhtw.paperless.dal.repositories.DocumentMetadataRepository;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UploadApiController implements UploadApi {

    final DocumentMetadataRepository documentMetadataRepository;
    final RabbitTemplate rabbitTemplate;
    final MinioClient minio;

    @Override
    public ResponseEntity<?> uploadPost(MultipartFile file, String description) {
        if (file.isEmpty() || !Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.badRequest().body("File is empty or not a PDF.");
        }

        try {
            if (!minio.bucketExists(BucketExistsArgs.builder().bucket("paperless").build())) {
                minio.makeBucket(MakeBucketArgs.builder().bucket("paperless").build());
            }

            minio.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket("paperless")
                            .object(file.getOriginalFilename()) // TODO: ensure no file path
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );

            // Load PDF file
            PDDocument document = Loader.loadPDF(file.getBytes());
            DocumentMetadata metadata = getDocumentMetadata(file, document);

            // Save metadata to the database
            documentMetadataRepository.save(metadata);

            document.close();

            rabbitTemplate.convertAndSend("documentQueue", Objects.requireNonNull(file.getOriginalFilename()));
            log.info("Message sent to RabbitMQ for document: {}", file.getOriginalFilename());

            return ResponseEntity.accepted().body("Metadata saved, document queued for processing");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing PDF file: " + e.getMessage());
        }
    }

    private static DocumentMetadata getDocumentMetadata(MultipartFile file, PDDocument document) {
        PDDocumentInformation info = document.getDocumentInformation();

        // Create DocumentMetadata instance
        DocumentMetadata metadata = new DocumentMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFilePath(""); // Add logic to set file path if needed
        metadata.setFileSize(String.valueOf(file.getSize()));
        metadata.setTitle(info.getTitle());
        metadata.setAuthor(info.getAuthor());
        metadata.setPageCount(document.getNumberOfPages());
        return metadata;
    }

}