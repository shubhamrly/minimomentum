package com.momentum.minimomentum.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for requesting a question-answer pair related to a transcript.
 * Contains the transcript ID and the question to be asked.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptQARequestDTO {

    private Long transcriptID;
    private String question;

}
