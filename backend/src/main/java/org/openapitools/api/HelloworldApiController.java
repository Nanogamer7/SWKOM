    package org.openapitools.api;



    import org.openapitools.api.HelloworldApi;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.context.request.NativeWebRequest;

    import javax.validation.constraints.*;
    import javax.validation.Valid;

    import java.util.List;
    import java.util.Map;
    import java.util.Optional;
    import javax.annotation.Generated;

    @Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-09-29T19:48:18.478427961+02:00[Europe/Vienna]", comments = "Generator version: 7.7.0")
    @Controller
    @RequestMapping("${openapi.paperless.base-path:}")
    public class HelloworldApiController implements HelloworldApi {

        private final NativeWebRequest request;

        @Autowired
        public HelloworldApiController(NativeWebRequest request) {
            this.request = request;
        }

        @Override
        public Optional<NativeWebRequest> getRequest() {
            return Optional.ofNullable(request);
        }

    }
