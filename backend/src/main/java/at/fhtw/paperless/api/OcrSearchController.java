package at.fhtw.paperless.api;

import at.fhtw.paperless.elasticsearch.OcrDocument;
import at.fhtw.paperless.elasticsearch.OcrDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OcrSearchController {

    private final OcrDocumentRepository ocrDocumentRepository;

    @GetMapping("/search-ocr")
    public ResponseEntity<List<OcrDocument>> searchOcrByText(@RequestParam String term) {

        List<OcrDocument> results = ocrDocumentRepository.findByTextContainingIgnoreCase(term);

        return ResponseEntity.ok(results);
    }
}
