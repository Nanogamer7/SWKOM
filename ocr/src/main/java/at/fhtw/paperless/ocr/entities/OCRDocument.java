package at.fhtw.paperless.ocr.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "ocr-text")
public class OCRDocument {

    @Id
    private String filename; //should be changed

    private String text;
}
