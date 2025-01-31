package at.fhtw.paperless.backend;

import at.fhtw.paperless.application.SearchService;
import at.fhtw.paperless.dal.models.OcrDocument;
import at.fhtw.paperless.dal.models.DocumentMetadata;
import at.fhtw.paperless.rest.controller.SearchController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(SearchController.class)
public class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Test
    void validSearchByFilename() throws Exception {
        List<DocumentMetadata> mockResults = List.of(createTestDocumentMetadata());

        when(searchService.searchFilenames("mock")).thenReturn(mockResults);

        mockMvc.perform(get("/search/by_filename").param("term", "mock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))  // ✅ Expect 1 result
                .andExpect(jsonPath("$[0].fileName").value("mock.pdf"))
                .andExpect(jsonPath("$[0].filePath").value("/test/mock.pdf"))
                .andExpect(jsonPath("$[0].fileSize").value("500KB"))
                .andExpect(jsonPath("$[0].scanned").value(true))
                .andExpect(jsonPath("$[0].uploadedAt").value("2025-01-01T10:00:00Z"));

    }

    @Test
    void searchForNonExistingFilename() throws Exception {
        when(searchService.searchFilenames("mock")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/search/by_filename").param("term", "mock"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchForExistingFileWithContent() throws Exception {
        List<OcrDocument> mockResults = List.of(createTestOcrDocument());

        when(searchService.searchContent("OCR")).thenReturn(mockResults);

        mockMvc.perform(get("/search/by_content").param("term", "OCR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].filename").value("mock.pdf"))
                .andExpect(jsonPath("$[0].text").value("this is some OCR text"));

    }

    @Test
    void searchForNonExistingFileWithContent() throws Exception {
        when(searchService.searchContent("OCR")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/search/by_filename").param("term", "mock"))
                .andExpect(status().isNotFound());
    }


    private DocumentMetadata createTestDocumentMetadata() {
        DocumentMetadata mockData = new DocumentMetadata();
        mockData.setId(UUID.randomUUID());
        mockData.setFileName("mock.pdf");
        mockData.setFilePath("/test/mock.pdf");
        mockData.setScanned(true);
        mockData.setFileSize("500KB");
        mockData.setUploadedAt(Instant.parse("2025-01-01T10:00:00Z"));

        return mockData;
    }

    private OcrDocument createTestOcrDocument() {
        OcrDocument mockData = new OcrDocument();
        mockData.setFilename("mock.pdf");
        mockData.setText("this is some OCR text");

        return mockData;
    }
}
