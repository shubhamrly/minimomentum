package com.momentum.minimomentum.repository;

import com.momentum.minimomentum.model.QuestionAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAnswersRepository extends MongoRepository<QuestionAnswer, String> {

    List<QuestionAnswer> findByTranscriptIdOrderByCreateDateTimeDesc(String transcriptId);

}
