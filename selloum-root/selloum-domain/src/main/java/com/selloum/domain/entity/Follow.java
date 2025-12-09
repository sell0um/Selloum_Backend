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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Table(name = "follow")
public class Follow {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 팔로우 건 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follower_id")
	private User follower;
	
	// 팔로우 당한 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_id")
	private User following;

}
