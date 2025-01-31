package at.fhtw.paperless.ocr.repositories;

import at.fhtw.paperless.ocr.entities.OCRDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface OCRDocumentRepository extends ElasticsearchRepository<OCRDocument, String> {
}
