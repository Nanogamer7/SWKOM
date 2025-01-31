package at.fhtw.paperless.application;

import at.fhtw.paperless.dal.models.DocumentMetadata;
import at.fhtw.paperless.dal.repositories.DocumentMetadataRepository;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    final DocumentMetadataRepository documentMetadataRepository;
    final RabbitTemplate rabbitTemplate;
    final MinioClient minio;

    public void handleFile(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!minio.bucketExists(BucketExistsArgs.builder().bucket("paperless").build())) {
            minio.makeBucket(MakeBucketArgs.builder().bucket("paperless").build());
        }

        DocumentMetadata metadata = null;

        // Load PDF file
        PDDocument document = Loader.loadPDF(file.getBytes());
        metadata = getDocumentMetadata(file, document);

        // Save metadata to the database
        documentMetadataRepository.save(metadata);

        minio.putObject(
                PutObjectArgs
                        .builder()
                        .bucket("paperless")
                        .object(metadata.getId().toString()) // TODO: ensure no file path
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build()
        );

        document.close();

        rabbitTemplate.convertAndSend("documentQueue", metadata.getId().toString());
        log.info("Message sent to RabbitMQ for document: {}", metadata.getId().toString());

    }

    public byte[] getFile(UUID uuid) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (InputStream is = minio.getObject(
                GetObjectArgs.builder()
                        .bucket("paperless")
                        .object(uuid.toString())
                        .build())) {

            return is.readAllBytes();
        }
    }

    public DocumentMetadata getMetadata(UUID uuid) {
        return documentMetadataRepository.findById(uuid).orElse(null);
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
