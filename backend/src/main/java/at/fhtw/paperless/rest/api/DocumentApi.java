package at.fhtw.paperless.rest.api;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/document")
public interface DocumentApi {

    @PostMapping
    ResponseEntity<?> upload(
            @Parameter(name = "file", description = "", required = true) @RequestPart(value = "file", required = true) MultipartFile file,
            @Parameter(name = "description", description = "") @Valid @RequestParam(value = "description", required = false) String description
    );

    @GetMapping("/{filename}")
    ResponseEntity<byte[]> downloadDocument(@PathVariable String filename);
}
