package com.momentum.minimomentum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "summaries")
@AllArgsConstructor
@NoArgsConstructor
public class Summary {
    @Id
    private String id;
    private String summary;
    private String transcriptId;
    private String language;
}
