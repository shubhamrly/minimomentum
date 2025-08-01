package com.momentum.minimomentum.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.util.List;
@Data
@Embeddable
public class SummaryDetails {

    private String summary;
    private String tone;
    private String outcome;

    @ElementCollection
    private List<String> whatWentWell;

    @ElementCollection
    private List<String> whatCouldBeImproved;

    @ElementCollection
    private List<String> objectionsOrDiscoveryInsights;

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
