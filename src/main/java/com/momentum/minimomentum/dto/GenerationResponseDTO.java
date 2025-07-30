package com.momentum.minimomentum.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerationResponseDTO {

    private String transcriptId;

    private String language;

    private String content;

    private LocalDateTime createdAt;

}