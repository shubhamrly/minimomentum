package com.momentum.minimomentum.dto.responseDTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDTO {
    @JsonProperty("Summary")
    private String summary;

    @JsonProperty("SummaryDetails")
    private SummaryDetailsDTO summaryDetails;
}

