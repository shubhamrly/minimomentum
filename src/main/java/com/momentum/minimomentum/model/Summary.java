package com.momentum.minimomentum.model;

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
    @Embedded
    @Lob
    private SummaryDetails summary;

    private String language;

    @ManyToOne
    @JoinColumn(name = "transcript_id")
    private Transcript transcript;
}
