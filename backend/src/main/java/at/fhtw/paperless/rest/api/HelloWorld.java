package at.fhtw.paperless.rest.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorld {
    @GetMapping
    public ResponseEntity<?> helloWorld() {
        return ResponseEntity.ok("Hello World!");
    }
}
