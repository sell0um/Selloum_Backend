package com.selloum.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.selloum.domain.entity.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
	
	Optional<Reaction> findByUser_IdAndDiary_DiaryId(Long userId, Long diaryId);
	boolean existsByUser_IdAndDiary_DiaryId(Long userId, Long diaryId);

}
