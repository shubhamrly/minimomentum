package com.momentum.minimomentum.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptQARequestDTO {

        private Long transcriptID;
        private String question;

    }
