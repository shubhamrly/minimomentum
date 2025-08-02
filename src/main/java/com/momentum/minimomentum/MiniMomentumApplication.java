package com.momentum.minimomentum;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(title = "MiniMomentum-Assignment", version = "v2"),
        tags = {
            @Tag(name = "1. Transcription", description = "Transcript APIs"),
            @Tag(name = "2. Summariser", description = "Summary APIs"),
            @Tag(name = "3. Question Answer", description = "Question Answer APIs")
        }
)
@SpringBootApplication
public class MiniMomentumApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniMomentumApplication.class, args);
    }

}
