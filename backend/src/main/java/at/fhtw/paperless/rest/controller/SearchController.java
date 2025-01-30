// SearchController.java
package at.fhtw.paperless.rest.controller;

import at.fhtw.paperless.dal.elasticsearch.OcrDocument;
import at.fhtw.paperless.dal.elasticsearch.OcrDocumentRepository;
import at.fhtw.paperless.dal.models.DocumentMetadata;
import at.fhtw.paperless.dal.repositories.DocumentMetadataRepository;
import at.fhtw.paperless.rest.api.SearchApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController implements SearchApi {
    private final DocumentMetadataRepository documentMetadataRepository;
    private final OcrDocumentRepository ocrDocumentRepository;

    @Override
    public ResponseEntity<List<DocumentMetadata>> byFilename(String term) {
        List<DocumentMetadata> results = documentMetadataRepository.findByFileNameContainingIgnoreCase(term);
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results);
    }

    @Override
    public ResponseEntity<List<OcrDocument>> byContent(String term) {
        // Hier verwenden wir die Methode: findByTextContainingIgnoreCase
        // die wir im OcrDocumentRepository definiert haben
        List<OcrDocument> results = ocrDocumentRepository.findByTextContainingIgnoreCase(term);
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results);
    }
}