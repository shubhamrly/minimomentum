package com.momentum.minimomentum.dto;

import lombok.Data;

@Data
public class TranscriptQAResponseDTO{
    String question;
    String answer;
    TranscriptResponseDTO transcriptText;
    TranscriptResponseDTO transcriptId;


}
