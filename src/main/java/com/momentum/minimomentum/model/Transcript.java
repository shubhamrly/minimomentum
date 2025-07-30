package com.momentum.minimomentum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "transcripts")
public class Transcript {
    @Id
    String id;
    String language;
    String content;
    LocalDateTime createdAt;

}
