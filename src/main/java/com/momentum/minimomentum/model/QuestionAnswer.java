package com.momentum.minimomentum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "question_answers")
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private String answer;

    private LocalDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name = "transcript_id")
    private Transcript transcript;

}

