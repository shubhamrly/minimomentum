package com.momentum.minimomentum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transcripts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transcript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String transcriptText;

    String language;

    LocalDateTime createDateTime;


    @OneToMany(mappedBy = "transcript", cascade = CascadeType.ALL)
    private List<Summary> summaries;


    @OneToMany(mappedBy = "transcript", cascade = CascadeType.ALL)
    private List<QuestionAnswer> questionAnswers;

    public Transcript(Long id, String transcriptText, String language, LocalDateTime createDateTime) {
    }
}
