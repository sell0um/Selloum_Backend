package com.selloum.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.selloum.domain.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
