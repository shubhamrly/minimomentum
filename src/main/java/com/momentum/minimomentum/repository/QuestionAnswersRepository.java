package com.momentum.minimomentum.repository;

import com.momentum.minimomentum.model.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Repository interface for managing QuestionAnswer entities.
 * Provides methods to find question answers by transcript ID, ordered by creation date.
 */

@Repository
public interface QuestionAnswersRepository extends JpaRepository<QuestionAnswer, Long> {

    List<QuestionAnswer> findByTranscriptIdOrderByCreateDateTimeDesc(Long transcriptId);

}
