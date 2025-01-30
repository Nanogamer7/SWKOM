package at.fhtw.paperless.application;

import at.fhtw.paperless.dal.elasticsearch.OcrDocument;
import at.fhtw.paperless.dal.elasticsearch.OcrDocumentRepository;
import at.fhtw.paperless.dal.models.DocumentMetadata;
import at.fhtw.paperless.dal.repositories.DocumentMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final DocumentMetadataRepository documentMetadataRepository;
    private final OcrDocumentRepository ocrDocumentRepository;

    public List<DocumentMetadata> searchFilenames(String searchTerm) {
        return documentMetadataRepository.findByFileNameContainingIgnoreCase(searchTerm);
    }

    public List<OcrDocument> searchContent(String searchTerm) {
        return ocrDocumentRepository.findByTextContainingIgnoreCase(searchTerm);
    }
}
