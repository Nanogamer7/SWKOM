package at.fhtw.paperless.ocr;

import at.fhtw.paperless.ocr.entities.OCRDocument;
import at.fhtw.paperless.ocr.repositories.OCRDocumentRepository;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueueListenerTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private OCRDocumentRepository ocrDocumentRepository;

    @InjectMocks
    private QueueListener queueListener;

    private final String testPdfName = "test.pdf";

    @BeforeEach
    void setup() {
        queueListener = new QueueListener(minioClient, rabbitTemplate, ocrDocumentRepository);
    }

    @Test
    void testReceivingMessageSuccessfully() throws Exception {

        byte[] pdfContent = new byte[]{37, 80, 68, 70, 45};
        InputStream pdfStream = new ByteArrayInputStream(pdfContent);

        // Mock MinIO response
        GetObjectResponse response = mock(GetObjectResponse.class);
        doReturn(response).when(minioClient).getObject(any(GetObjectArgs.class));


        QueueListener spyListener = spy(queueListener);
        doReturn(List.of()).when(spyListener).convertPDFToImage(any());
        doReturn("Mock OCR text").when(spyListener).extractTextFromImages(any());

        spyListener.receive(testPdfName);

        // Verify OCRDocument is saved
        ArgumentCaptor<OCRDocument> documentCaptor = ArgumentCaptor.forClass(OCRDocument.class);
        verify(ocrDocumentRepository).save(documentCaptor.capture());

        OCRDocument savedDocument = documentCaptor.getValue();
        assertEquals(testPdfName, savedDocument.getFilename());
        assertEquals("Mock OCR text", savedDocument.getText());
    }

    @Test
    void testReceivingMessageFailure() throws Exception {
        when(minioClient.getObject(any(GetObjectArgs.class))).thenThrow(new RuntimeException("MinIO error"));

        Exception exception = assertThrows(Exception.class, () -> queueListener.receive(testPdfName));

        assertTrue(exception.getMessage().contains("MinIO error"));
    }
}
