package com.momentum.minimomentum.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
    * DTO for detailed summary information.
    * Contains various fields such as tone, outcome, strengths, improvements,
    * insights, churn risk signals, action points, agent, and customer.
    * The ChurnRiskSignalsDTO is an inner class that holds risk level and signals.
    * This class is used to encapsulate the detailed summary information
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummaryDetailsDTO {

    @JsonProperty("tone")
    public String tone;
    @JsonProperty("outcome")
    public String outcome;
    @JsonProperty("strengths")
    public List<String> strengths;
    @JsonProperty("improvements")
    public List<String> improvements;
    @JsonProperty("insights")
    public List<String> Insights;
    @JsonProperty("churnRiskSignals")
    public ChurnRiskSignalsDTO churnRiskSignals;
    @JsonProperty("actionPoints")
    public List<String> actionPoints;
    @JsonProperty("agent")
    public String agent;
    @JsonProperty("customer")
    public String customer;

    @Data
    public static class ChurnRiskSignalsDTO {

        @JsonProperty("riskLevel")
        public String riskLevel;
        @JsonProperty("signals")
        public List<String> signals;
    }
}
