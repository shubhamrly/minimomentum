package com.momentum.minimomentum.model;

import com.momentum.minimomentum.dto.SummaryDetailsDTO;
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
    private SummaryDetailsDTO summary;
    private String transcriptId;
    private String language;
}
