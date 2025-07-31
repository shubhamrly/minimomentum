package com.momentum.minimomentum.repository;

import com.momentum.minimomentum.model.QuestionAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswers  extends MongoRepository<QuestionAnswer, String> {


}
