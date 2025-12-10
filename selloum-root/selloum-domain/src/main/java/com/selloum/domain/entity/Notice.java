package com.selloum.domain.entity;

import com.selloum.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Notice")

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long noticeId;
	
	@Column(nullable = false, unique = false)
	private String name;

	@Column(nullable = false, unique = false)
	private String content;
	
	@Column(nullable = true)
	private String imagePath;
	
	
}
