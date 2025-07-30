package com.momentum.minimomentum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryResponseDTO {
    private String summaryId;
    private String summary;
    private String transcriptId;
    private String language;

}
