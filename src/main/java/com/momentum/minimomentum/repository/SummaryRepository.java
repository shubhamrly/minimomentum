package com.momentum.minimomentum.repository;

import com.momentum.minimomentum.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SummaryRepository extends JpaRepository<Summary, Long> {
    Optional<Summary> findByTranscriptIdAndLanguage(Long transcriptId, String language);
}
