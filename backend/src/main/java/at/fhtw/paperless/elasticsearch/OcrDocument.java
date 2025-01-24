package at.fhtw.paperless.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "ocr-text")
public class OcrDocument {

    @Id
    private String filename;

    private String text;
}
