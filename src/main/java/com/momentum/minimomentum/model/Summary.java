package com.momentum.minimomentum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a summary of a transcript. Contains the summary text,
 * detailed summary information, language, and a reference to the associated
 * transcript. This entity is used to store the summarized content of a
 * transcript along with its details. It is mapped to the "summaries" table in
 * the database. * The summary text is stored as a large object (LOB) to
 * accommodate potentially long summaries. * The summary details are embedded
 * within this entity, allowing for structured storage of additional
 * information.
 */
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
