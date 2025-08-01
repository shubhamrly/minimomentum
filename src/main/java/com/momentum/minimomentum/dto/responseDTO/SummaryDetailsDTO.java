package com.momentum.minimomentum.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryDetailsDTO {
    @JsonProperty("Summary")
    public String summary;
    @JsonProperty("Tone")
    public String tone;
    @JsonProperty("Outcome")
    public String outcome;
    @JsonProperty("WhatWentWell")
    public List<String> whatWentWell;
    @JsonProperty("WhatCouldBeImproved")
    public List<String> whatCouldBeImproved;
    @JsonProperty("ObjectionsOrDiscoveryInsights")
    public List<String> objectionsOrDiscoveryInsights;
    @JsonProperty("ChurnRiskSignals")
    public ChurnRiskSignalsDTO churnRiskSignals;
    @JsonProperty("ActionPoints")
    public List<String> actionPoints;

    @Data
    public static class ChurnRiskSignalsDTO{
        @JsonProperty("RiskLevel")
        public String riskLevel;
        @JsonProperty("Signals")
        public List<String> signals;
    }
}

