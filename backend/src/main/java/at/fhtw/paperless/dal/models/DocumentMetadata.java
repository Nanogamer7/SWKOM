package at.fhtw.paperless.dal.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
public class DocumentMetadata {
    @Id
    @GeneratedValue
    private UUID id;

    private String fileName;
    private String filePath;
    private String fileSize;
    private boolean scanned = false;

    private String title;
    private String author;
    private Integer pageCount;

    @CreationTimestamp
    private Instant uploadedAt;
    private Instant createdAt;
    private Instant updatedAt;
}