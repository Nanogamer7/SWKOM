package at.fhtw.paperless.ocr;

import at.fhtw.paperless.ocr.config.RabbitMQConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;




@Component
@RequiredArgsConstructor
public class QueueListener {

    private final MinioClient minioClient;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receive(String filename) {
        System.out.println(filename);

        try (InputStream stream = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket("paperless")
                .object(filename)
                .build())) {


            //Convert PDF to image as tesseract does not support pdfs
            List<BufferedImage> images = convertPDFToImage(stream);

            String text = exctractTextFromImages(images);

            System.out.println(text);

            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, text);

        } catch (Exception e) {
            System.out.println(e.getMessage());
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

}
