package com.momentum.minimomentum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a question-answer pair related to a transcript.
 * Contains the question, answer, creation date-time, and a reference to the associated transcript.
 */

@Data
@Entity
@Table(name = "question_answers")
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String question;
    @Lob
    private String answer;

    private LocalDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name = "transcript_id")
    @JsonIgnore
    private Transcript transcript;

}
