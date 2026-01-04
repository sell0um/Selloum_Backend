package com.selloum.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Table(name = "follow")
public class Follow {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 팔로우 신청을 건 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_user_id")
	private User from;
	
	// 팔로우 신청 받은 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id")
	private User to;
	
	// 팔로우 상태
	// 신청 / 수락 구분
	private String status;
	
	
	public void acceptFollow() {
		this.status = "APPLY";
	}

}
