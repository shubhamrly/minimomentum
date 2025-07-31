package com.momentum.minimomentum.dto.requestDTO;

import com.momentum.minimomentum.dto.TranscriptResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptQARequestDTO {

        private TranscriptResponseDTO transcriptID;

        private String question;
        private String answer;

    }
