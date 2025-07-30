package com.momentum.minimomentum.repository;

import com.momentum.minimomentum.model.Summary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SummaryRepository extends MongoRepository<Summary, String> {

    Optional<Summary> findByTranscriptIdAndLanguage(String transcriptId, String language);
}
