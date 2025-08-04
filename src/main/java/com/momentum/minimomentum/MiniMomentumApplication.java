package com.momentum.minimomentum;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(title = "MiniMomentum-Assignment", version = "v2"),
        tags = {
            @Tag(name = "Transcription", description = "Transcript APIs"),
            @Tag(name = "Summariser", description = "Summary APIs"),
            @Tag(name = "Question Answer", description = "Question Answer APIs")
        }
)
@SpringBootApplication
public class MiniMomentumApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniMomentumApplication.class, args);
    }

}
