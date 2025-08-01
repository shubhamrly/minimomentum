package com.momentum.minimomentum.model;

import jakarta.persistence.*;
import java.util.List;

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
    public static class ChurnRiskSignals {
        private String riskLevel;

        @ElementCollection
        private List<String> signals;
    }
}
