package at.fhtw.paperless.dal.elasticsearch;

import at.fhtw.paperless.dal.models.OcrDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface OcrDocumentRepository extends ElasticsearchRepository<OcrDocument, String> {

    List<OcrDocument> findByTextContainingIgnoreCase(String term);
}
