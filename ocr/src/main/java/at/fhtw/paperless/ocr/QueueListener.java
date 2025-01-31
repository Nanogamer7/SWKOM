package at.fhtw.paperless.ocr;

import at.fhtw.paperless.ocr.config.RabbitMQConfig;
import at.fhtw.paperless.ocr.entities.OCRDocument;
import at.fhtw.paperless.ocr.repositories.OCRDocumentRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;




@Component
@RequiredArgsConstructor
@Log
public class QueueListener {

    private final MinioClient minioClient;
    private final RabbitTemplate rabbitTemplate;
    private final OCRDocumentRepository ocrDocumentRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receive(String message) {
        log.info("Received message: " + message);

        try (InputStream stream = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket("paperless")
                .object(message)
                .build())) {


            //Convert PDF to image as tesseract does not support pdfs
            List<BufferedImage> images = convertPDFToImage(stream);

            String text = exctractTextFromImages(images);
            log.info("Finished processing");

            saveToElasticSearch(message, text);

            rabbitTemplate.convertAndSend(RabbitMQConfig.OUTPUT_QUEUE_NAME, message);

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<BufferedImage> convertPDFToImage(InputStream inputStream) throws IOException {
        PDDocument pdf = PDDocument.load(inputStream);
        PDFRenderer renderer = new PDFRenderer(pdf);

        List<BufferedImage> images = new ArrayList<>();

        for(int i = 0; i < pdf.getNumberOfPages(); i++) {
            BufferedImage pageImage = renderer.renderImage(i);
            images.add(pageImage);
        }

        pdf.close();
        return images;
    }

    private String extractTextFromSingleImage(BufferedImage image) {
        Tesseract tesseract = new Tesseract();

        String datapath = System.getenv("TESSDATA_PREFIX");

        if (datapath == null)
            throw new RuntimeException("Tesseract data path not set correctly");

        try{
            tesseract.setDatapath(datapath + "tessdata");

            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }

    private String exctractTextFromImages(List<BufferedImage> images) {
        StringBuilder output = new StringBuilder();

        for(BufferedImage image : images) {
            output.append(extractTextFromSingleImage(image));
        }

        return output.toString();
    }

    private void saveToElasticSearch(String filename, String text) {
        OCRDocument document = new OCRDocument();
        document.setFilename(filename);
        document.setText(text);

        ocrDocumentRepository.save(document);
    }
}