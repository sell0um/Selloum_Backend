package com.selloum.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.selloum.domain.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "user")
@Builder
public class User extends BaseEntity {
	
	/*
	 * 테이블 필드 
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, unique = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String email;
	
	private String phone;
	
//	private String profileImage;
	
	private String Role;
	
	
//	/*
//	 * 연관관계 관련 필드
//	 */
//	
//	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER , cascade = CascadeType.ALL, orphanRemoval = false)
//	@ToString.Exclude
//	private List<Diary> diaryList = new ArrayList<>();
//	
//	@OneToMany(mappedBy = "user")
//	@ToString.Exclude
//	private List<Report> reportList = new ArrayList<>();
//	
	
	
}
