// SearchController.java
package at.fhtw.paperless.rest.controller;

import at.fhtw.paperless.application.SearchService;
import at.fhtw.paperless.dal.elasticsearch.OcrDocument;
import at.fhtw.paperless.dal.models.DocumentMetadata;
import at.fhtw.paperless.rest.api.SearchApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController implements SearchApi {
    private final SearchService searchService;

    @Override
    public ResponseEntity<List<DocumentMetadata>> byFilename(String term) {
        List<DocumentMetadata> results = searchService.searchFilenames(term);
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results);
    }

    @Override
    public ResponseEntity<List<OcrDocument>> byContent(String term) {
        List<OcrDocument> results = searchService.searchContent(term);
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results);
    }
}