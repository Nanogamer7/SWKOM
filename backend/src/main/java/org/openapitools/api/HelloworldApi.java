    /**
     * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.7.0).
     * https://openapi-generator.tech
     * Do not edit the class manually.
     */
    package org.openapitools.api;

    import io.swagger.v3.oas.annotations.ExternalDocumentation;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.Parameter;
    import io.swagger.v3.oas.annotations.Parameters;
    import io.swagger.v3.oas.annotations.media.ArraySchema;
    import io.swagger.v3.oas.annotations.media.Content;
    import io.swagger.v3.oas.annotations.media.Schema;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import io.swagger.v3.oas.annotations.security.SecurityRequirement;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import io.swagger.v3.oas.annotations.enums.ParameterIn;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.context.request.NativeWebRequest;
    import org.springframework.web.multipart.MultipartFile;
    import org.openapitools.api.ApiUtil;

    import javax.validation.Valid;
    import javax.validation.constraints.*;
    import java.util.List;
    import java.util.Map;
    import java.util.Optional;
    import javax.annotation.Generated;

    @Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-09-29T19:48:18.478427961+02:00[Europe/Vienna]", comments = "Generator version: 7.7.0")
    @Validated
    @Tag(name = "helloworld", description = "the helloworld API")
    public interface HelloworldApi {

        default Optional<NativeWebRequest> getRequest() {
            return Optional.empty();
        }

        /**
         * GET /helloworld : Test response
         *
         * @return Hello, World! (status code 200)
         */
        @Operation(
            operationId = "helloworldGet",
            summary = "Test response",
            responses = {
                @ApiResponse(responseCode = "200", description = "Hello, World!", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
                })
            }
        )
        @RequestMapping(
            method = RequestMethod.GET,
            value = "/helloworld",
            produces = { "text/plain" }
        )

        default ResponseEntity<String> helloworldGet(

        ) {
            return ResponseEntity.ok("Hello World!");
        }

    }
