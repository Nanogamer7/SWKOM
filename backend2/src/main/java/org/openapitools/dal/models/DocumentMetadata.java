package org.openapitools.dal.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.hibernate.annotations.*;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
public class DocumentMetadata {
    @javax.persistence.Id
    @Id
    @GeneratedValue
    private UUID id;

    private String file_name;
    private String file_path;
    private String file_size;

    private String title;
    private String author;
    private Integer page_count;

    @CreationTimestamp
    private Instant uploaded_at;
    private Instant created_at;
    private Instant updated_at;
}
