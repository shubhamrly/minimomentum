package com.momentum.minimomentum.dto.responseDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptResponseDTO {

    private Long id;

    private String transcriptText ;

    private String language;

    private LocalDateTime createDateTime;

}