package com.momentum.minimomentum.repository;

import com.momentum.minimomentum.model.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Repository interface for managing Transcripts in the database.
 * It extends JpaRepository to provide CRUD operations and query methods.
 */
@Repository
public interface TranscriptionRepository extends JpaRepository<Transcript, Long> {

}
