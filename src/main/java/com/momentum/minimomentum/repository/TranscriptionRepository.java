package com.momentum.minimomentum.repository;

import com.momentum.minimomentum.model.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptionRepository extends JpaRepository<Transcript, Long> {

}
