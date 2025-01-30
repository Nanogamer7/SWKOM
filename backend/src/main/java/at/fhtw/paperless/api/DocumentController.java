package at.fhtw.paperless.api;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
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
    public ResponseEntity<Resource> viewDocument(@PathVariable String filename) {
        try {
            InputStream is = minio.getObject(
                    GetObjectArgs.builder()
                            .bucket("paperless")
                            .object(filename)
                            .build()
            );

            InputStreamResource resource = new InputStreamResource(is);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/document/download/{filename}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable String filename) {
        try {
            InputStream is = minio.getObject(
                    GetObjectArgs.builder()
                            .bucket("paperless")
                            .object(filename)
                            .build()
            );

            InputStreamResource resource = new InputStreamResource(is);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}