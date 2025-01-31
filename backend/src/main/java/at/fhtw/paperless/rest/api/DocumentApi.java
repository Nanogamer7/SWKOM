package at.fhtw.paperless.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequestMapping("/document")
@Validated
public interface DocumentApi {

    @PostMapping
    @Operation(
            summary = "Upload a pdf document",
            description = "Upload a MultipartFile to be stored and have its contents scanned",
            requestBody = @RequestBody(
                    description = "Pdf document to upload",
                    required = true,
                    content = @Content(
                            mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "File was saved and its contents will be scanned",
                    content = @Content()
            )
    })
    ResponseEntity<?> upload(
            @Parameter(name = "file", description = "", required = true) @RequestPart(value = "file", required = true) MultipartFile file,
            @Parameter(name = "description", description = "") @Valid @RequestParam(value = "description", required = false) String description
    );

    @GetMapping("/{uuid}")
    @Operation(
            summary = "Download a pdf document",
            description = "Download a PDF document by it's name"
    )
    @ApiResponse(
            responseCode = "200",
            description = "The requested file",
            content = @Content(mediaType = "application/pdf")
    )
    ResponseEntity<byte[]> downloadDocument(@PathVariable UUID uuid);
}
