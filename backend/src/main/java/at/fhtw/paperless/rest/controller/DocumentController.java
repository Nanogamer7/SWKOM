package at.fhtw.paperless.rest.controller;

import at.fhtw.paperless.application.DocumentService;
import at.fhtw.paperless.rest.api.DocumentApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DocumentController implements DocumentApi {
    private final DocumentService documentService;

    @Override
    public ResponseEntity<?> upload(MultipartFile file, String description) {
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

    public ResponseEntity<byte[]> downloadDocument(@PathVariable String filename) {
        try {
            byte[] content = documentService.getFile(filename);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}