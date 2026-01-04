package com.selloum.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.selloum.domain.entity.Emotion;

public interface EmotionRepository extends JpaRepository<Emotion, Integer> {
	
	Optional<Emotion> findByType(String type);

}
