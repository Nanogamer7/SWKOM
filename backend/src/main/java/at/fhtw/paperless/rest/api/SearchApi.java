package at.fhtw.paperless.rest.api;

import at.fhtw.paperless.dal.models.OcrDocument;
import at.fhtw.paperless.dal.models.DocumentMetadata;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/search")
public interface SearchApi {

    @GetMapping("/by_filename")
    @Operation(
            summary = "Search a document by filename",
            description = "Search a document by its filename in the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Found at least one file matching the search term",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentMetadata.class)), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Found no files matching the search term",
                    content = @Content()
            )}
    )
    ResponseEntity<List<DocumentMetadata>> byFilename(@RequestParam String term);

    @GetMapping("/by_content")
    @Operation(
            summary = "Search a document by filename",
            description = "Search a document by its scanned content. Files are not immediately available for content search after uploading"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Found at least one file matching the search term",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OcrDocument.class)), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Found no files matching the search term",
                    content = @Content()
            )}
    )
    ResponseEntity<List<OcrDocument>> byContent(@RequestParam String term);
}
