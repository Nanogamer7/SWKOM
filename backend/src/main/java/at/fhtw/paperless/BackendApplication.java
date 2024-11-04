package at.fhtw.paperless;

import com.fasterxml.jackson.databind.Module;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
    nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean(name = "at.fhtw.paperless.OpenApiGeneratorApplication.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}