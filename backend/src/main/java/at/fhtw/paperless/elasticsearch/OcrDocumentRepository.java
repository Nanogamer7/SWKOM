package at.fhtw.paperless.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface OcrDocumentRepository extends ElasticsearchRepository<OcrDocument, String> {

    List<OcrDocument> findByTextContainingIgnoreCase(String term);
}
