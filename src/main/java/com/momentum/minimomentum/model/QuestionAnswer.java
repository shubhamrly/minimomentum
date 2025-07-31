package com.momentum.minimomentum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection = "question_answers")
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswer {

    @Id
    private String id;

    @Field("transcript_id")
    private String transcriptId; // Acts as foreign key

    private String question;
    private String answer;

    private LocalDateTime createdAt;

}

