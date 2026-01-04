package com.selloum.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.selloum.domain.entity.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

	// 사용자를 팔로우 한 사람
	List<Follow> findByStatusAndFrom_IdOrStatusAndTo_Id(
		    String status1, Long fromUserId,
		    String status2, Long toUserId
		);
}
