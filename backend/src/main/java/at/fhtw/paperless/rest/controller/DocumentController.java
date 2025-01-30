package at.fhtw.paperless.rest.controller;

import at.fhtw.paperless.dal.models.DocumentMetadata;
import at.fhtw.paperless.dal.repositories.DocumentMetadataRepository;
import at.fhtw.paperless.rest.api.DocumentApi;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DocumentController implements DocumentApi {

    final DocumentMetadataRepository documentMetadataRepository;
    final RabbitTemplate rabbitTemplate;
    final MinioClient minio;

    @Override
    public ResponseEntity<?> upload(MultipartFile file, String description) {
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

    public ResponseEntity<byte[]> downloadDocument(@PathVariable String filename) {
        try (InputStream is = minio.getObject(
                GetObjectArgs.builder()
                        .bucket("paperless")
                        .object(filename)
                        .build()
        )) {

            byte[] content = is.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            // PDF-Typ
            headers.setContentType(MediaType.APPLICATION_PDF);
            // "attachment": erzwingt Download
            headers.setContentDispositionFormData("attachment", filename);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}