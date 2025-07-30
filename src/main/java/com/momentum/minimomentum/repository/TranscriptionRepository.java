package com.momentum.minimomentum.repository;

import com.momentum.minimomentum.model.Transcript;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptionRepository extends MongoRepository<Transcript, String> {

}
