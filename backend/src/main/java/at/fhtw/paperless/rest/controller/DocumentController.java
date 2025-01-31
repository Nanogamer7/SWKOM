package at.fhtw.paperless.rest.controller;

import at.fhtw.paperless.application.DocumentService;
import at.fhtw.paperless.dal.models.DocumentMetadata;
import at.fhtw.paperless.rest.api.DocumentApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Log
public class DocumentController implements DocumentApi {
    private final DocumentService documentService;

    @Override
    public ResponseEntity<?> upload(MultipartFile file, String description) {
        log.info("Received a request to upload a file " + file.getOriginalFilename() + " with description " + description);
        if (file.isEmpty() || !Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.badRequest().body("File is empty or not a PDF.");
        }
        try {
            documentService.handleFile(file);
            return ResponseEntity.accepted().body("Metadata saved, document queued for processing");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing PDF file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Resource> viewDocument(@PathVariable UUID uuid) {
        log.info("Received a request to view a file " + uuid);
        try (InputStream is = new ByteArrayInputStream(documentService.getFile(uuid))) {
            InputStreamResource content = new InputStreamResource(is);
            DocumentMetadata metadata = documentService.getMetadata(uuid);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "inline; filename=" + metadata.getFileName());

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID uuid) {
        log.info("Received a request to download a file " + uuid);
        try (InputStream is = new ByteArrayInputStream(documentService.getFile(uuid))) {
            InputStreamResource content = new InputStreamResource(is);
            DocumentMetadata metadata = documentService.getMetadata(uuid);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "attachment; filename=" + metadata.getFileName());

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}