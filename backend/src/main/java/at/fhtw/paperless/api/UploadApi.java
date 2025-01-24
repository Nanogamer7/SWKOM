/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.7.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.fhtw.paperless.api;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("${openapi.paperless.base-path:}")
@Validated
public interface UploadApi {

    @PostMapping("/upload")
    default ResponseEntity<?> uploadPost(
            @Parameter(name = "file", description = "", required = true) @RequestPart(value = "file", required = true) MultipartFile file,
            @Parameter(name = "description", description = "") @Valid @RequestParam(value = "description", required = false) String description
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}