package com.momentum.minimomentum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "summaries")
@AllArgsConstructor
@NoArgsConstructor
public class Summary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String summaryText;
    @Embedded
    private SummaryDetails summaryDetails;

    private String language;

    @ManyToOne
    @JoinColumn(name = "transcript_id")
    @JsonIgnore
    private Transcript transcript;
}
