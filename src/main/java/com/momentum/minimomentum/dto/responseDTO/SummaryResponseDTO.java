package com.momentum.minimomentum.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryResponseDTO {

    private Long summaryId;
    private String summaryText;
    private SummaryDetailsDTO summaryDetails;
    private String transcriptId;
    private String language;

}
