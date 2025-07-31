package com.momentum.minimomentum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class SummaryDetailsDTO {
    @JsonProperty("Summary")
    public String summary;
    @JsonProperty("Tone")
    public String tone;
    @JsonProperty("Outcome")
    public String outcome;
    @JsonProperty("WhatWentWell")
    public ArrayList<String> whatWentWell;
    @JsonProperty("WhatCouldBeImproved")
    public ArrayList<String> whatCouldBeImproved;
    @JsonProperty("ObjectionsOrDiscoveryInsights")
    public ArrayList<String> objectionsOrDiscoveryInsights;
    @JsonProperty("ChurnRiskSignals")
    public ChurnRiskSignalsDTO churnRiskSignals;
    @JsonProperty("ActionPoints")
    public ArrayList<String> actionPoints;

    public static class ChurnRiskSignalsDTO{
        @JsonProperty("RiskLevel")
        public String riskLevel;
        @JsonProperty("Signals")
        public ArrayList<String> signals;
    }
}

