package com.selloum.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.selloum.domain.entity.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>{
	
	
    @Query("""
            SELECT d FROM Diary d
            WHERE d.user.id = :userId
              AND (:year IS NULL OR YEAR(d.createdAt) = :year)
              AND (:month IS NULL OR MONTH(d.createdAt) = :month)
            ORDER BY d.createdAt ASC
        """)
	List<Diary> findAllByUserAndDate(
					@Param("userId")long userId, 
					@Param("year")Integer year, 
					@Param("month")Integer month);

}
