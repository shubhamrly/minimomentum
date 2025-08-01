package com.momentum.minimomentum.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranscriptQAResponseDTO{

    String id;
    String answer;
    String question;
    Long transcriptId;
    LocalDateTime createDateTime;

}
