package com.selloum.domain.entity;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.selloum.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Table(name = "user")
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

	@Column(nullable = false)
	private String role;
	
	private String phone;
	
	private String status;
	
	
    @Builder  // 생성자에 직접 붙이기
    public User(String name, String username, String password, 
                String email, String role, String phone, String status) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.status = status;
    }
	
	
	
//	private String profileImage;
	
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
