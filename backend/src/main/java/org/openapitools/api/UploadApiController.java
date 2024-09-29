package org.openapitools.api;


import org.openapitools.api.UploadApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-09-29T19:48:18.478427961+02:00[Europe/Vienna]", comments = "Generator version: 7.7.0")
@Controller
@RequestMapping("${openapi.paperless.base-path:}")
public class UploadApiController implements UploadApi {

    private final NativeWebRequest request;

    @Autowired
    public UploadApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
