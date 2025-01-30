package at.fhtw.paperless.rest.api;

import at.fhtw.paperless.dal.elasticsearch.OcrDocument;
import at.fhtw.paperless.dal.models.DocumentMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/search")
public interface SearchApi {

    @GetMapping("/by_filename")
    ResponseEntity<List<DocumentMetadata>> byFilename(@RequestParam String term);

    @GetMapping("/by_content")
    ResponseEntity<List<OcrDocument>> byContent(@RequestParam String term);
}
