package com.momentum.minimomentum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a transcript.
 * Contains the transcript text, language, and creation date-time.
 */

@Entity
@Table(name = "transcripts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transcript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Lob
    String transcriptText;

    String language;

    LocalDateTime createDateTime;

}
