package at.fhtw.paperless.dal.repositories;

import at.fhtw.paperless.dal.models.DocumentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata, UUID> {
    // Diese Methode fügt die Suchfunktion hinzu
    List<DocumentMetadata> findByFileNameContainingIgnoreCase(String term);

    @Modifying
    @Transactional
    @Query("UPDATE DocumentMetadata d SET d.scanned = :scanned WHERE LOWER(d.fileName) LIKE LOWER(CONCAT('%', :fileName, '%'))")
    int updateScannedByFileName(String fileName, boolean scanned);
}