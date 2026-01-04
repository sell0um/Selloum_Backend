package com.selloum.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.selloum.domain.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
	
	List<Report> findByUser_Id(long userId);
	List<Report> findByUser_IdAndType(long userId, String type);   // WEEKLY

}
