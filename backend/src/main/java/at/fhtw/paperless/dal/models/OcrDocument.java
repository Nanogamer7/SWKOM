package at.fhtw.paperless.dal.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Data
@Document(indexName = "ocr-text")
public class OcrDocument {

    @Id
    private String filename;
    private String text;

    @Transient
    @JsonSerialize
    private UUID id;
}
