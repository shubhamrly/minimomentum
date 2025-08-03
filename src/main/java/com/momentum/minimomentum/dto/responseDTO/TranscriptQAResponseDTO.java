package com.momentum.minimomentum.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for the response of a question-answer pair related to a transcript.
 * Contains the ID, answer, question, transcript ID, and creation date-time.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranscriptQAResponseDTO {

    String id;
    String answer;
    String question;
    Long transcriptId;
    LocalDateTime createDateTime;

}
