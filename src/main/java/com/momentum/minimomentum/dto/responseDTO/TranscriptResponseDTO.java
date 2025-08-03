package com.momentum.minimomentum.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/*
 * DTO for the response of a transcript request.
 * Contains the transcript ID, transcript text, language, and creation date-time.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptResponseDTO {

    private Long transcriptId;

    private String transcriptText;

    private String language;

    private LocalDateTime createDateTime;

}
