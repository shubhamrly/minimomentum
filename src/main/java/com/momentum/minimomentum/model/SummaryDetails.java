package com.momentum.minimomentum.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.util.List;

/**
 * Entity representing detailed summary information of a transcript. Contains
 * tone, outcome, agent, customer details, strengths, improvements, insights,
 * churn risk signals, and action points.
 */
@Data
@Embeddable
public class SummaryDetails {

    private String tone;
    private String outcome;
    private String agent;
    private String customer;

    @ElementCollection
    private List<String> strengths;

    @ElementCollection
    private List<String> improvements;

    @ElementCollection
    private List<String> Insights;

    @Embedded
    private ChurnRiskSignals churnRiskSignals;

    @ElementCollection
    private List<String> actionPoints;

    @Embeddable
    @Data
    public static class ChurnRiskSignals {

        private String riskLevel;

        @ElementCollection
        private List<String> signals;
    }
}
