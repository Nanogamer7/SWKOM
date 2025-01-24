package at.fhtw.paperless.api;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private final MinioClient minio;

    @GetMapping("/document/{filename}")
    public ResponseEntity<byte[]> getDocument(@PathVariable String filename) {
        // Dies ist dein bestehender „Ansehen“-Endpoint
        // der "inline" anzeigt, falls du es so konfiguriert hast.
        // (Nur zur Info, nicht verändert.)
        // ...
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/document/download/{filename}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String filename) {
        try (InputStream is = minio.getObject(
                GetObjectArgs.builder()
                        .bucket("paperless")
                        .object(filename)
                        .build()
        )) {

            byte[] content = is.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            // PDF-Typ
            headers.setContentType(MediaType.APPLICATION_PDF);
            // "attachment": erzwingt Download
            headers.setContentDispositionFormData("attachment", filename);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
