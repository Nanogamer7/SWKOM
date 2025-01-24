// SearchController.java
package at.fhtw.paperless.api;

import at.fhtw.paperless.dal.models.DocumentMetadata;
import at.fhtw.paperless.dal.repositories.DocumentMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final DocumentMetadataRepository documentMetadataRepository;

    @GetMapping("/search")
    public ResponseEntity<List<DocumentMetadata>> search(@RequestParam String term) {
        List<DocumentMetadata> results = documentMetadataRepository.findByFileNameContainingIgnoreCase(term);
        return ResponseEntity.ok(results);
    }
}