package at.fhtw.paperless.backend;

import at.fhtw.paperless.application.DocumentService;
import at.fhtw.paperless.rest.controller.DocumentController;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;



@WebMvcTest(DocumentController.class)
@AutoConfigureMockMvc
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public DocumentService documentService;

    @Test
    void uploadValidPDF() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.pdf",
                "application/pdf", "Test-Pdf Content".getBytes());

        doNothing().when(documentService).handleFile(any());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/document")
                .file(mockFile)
                .param("description", "test file"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Metadata saved, document queued for processing"));
    }

    @Test
    void uploadInvalidFileType() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Test-PDF Content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/document")
                .file(mockFile)
                .param("description", "test file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("File is empty or not a PDF."));
    }

    @Test
    void uploadEmptyFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "empty.pdf",
                "application/pdf", new byte[0]);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/document")
                .file(mockFile)
                .param("description", "test file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("File is empty or not a PDF."));
    }

    @Test
    void uploadThrowsException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "Dummy PDF Content".getBytes()
        );

        doThrow(new RuntimeException("Service error")).when(documentService).handleFile(any());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/document")
                        .file(file)
                        .param("description", "Test error"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error processing PDF file: Service error"));

    }

    @Test
    void downloadPDF() throws Exception {
        String filename = "sample.pdf";
        byte[] fileContent = "unittests are so much fun ;-;".getBytes();

        when(documentService.getFile(filename)).thenReturn(fileContent);

        mockMvc.perform(MockMvcRequestBuilders.get("/document/{filename}", filename))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"" + filename + "\""))  // ✅ Adjusted expected value
                .andExpect(content().bytes(fileContent));
    }

    @Test
    void downloadPdfException() throws Exception {
        String filename = "error.pdf";

        when(documentService.getFile(filename)).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/document/{filename}", filename))
                .andExpect(status().isInternalServerError());
    }
}
