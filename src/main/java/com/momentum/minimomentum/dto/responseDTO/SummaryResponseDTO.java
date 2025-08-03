package com.momentum.minimomentum.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the response of a summary request. Contains the summary ID, summary
 * text, detailed summary information, transcript ID, and language of the
 * summary.
 */
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
