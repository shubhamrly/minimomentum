package com.momentum.minimomentum.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDTO {

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("summaryDetails")
    private SummaryDetailsDTO summaryDetails;
}
