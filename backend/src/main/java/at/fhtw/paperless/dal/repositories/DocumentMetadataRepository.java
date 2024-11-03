package at.fhtw.paperless.dal.repositories;

import at.fhtw.paperless.dal.models.DocumentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata, UUID> {
}
