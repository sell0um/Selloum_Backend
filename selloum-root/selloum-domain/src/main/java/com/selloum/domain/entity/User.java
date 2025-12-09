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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
	

	
	// FK - 일단 양방향 매핑
    // 사용하지 않는다면 추후 단방향 매핑으로 변경할 것
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER , cascade = CascadeType.ALL, orphanRemoval = false)
	@ToString.Exclude
	private List<Diary> diaryList = new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	@ToString.Exclude
	private List<Report> reportList = new ArrayList<>();
	
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reaction = new ArrayList<>();
	
    // 내가 팔로우한 목록
    @OneToMany(mappedBy = "follower")
    private List<Follow> followings = new ArrayList<>();

    // 나를 팔로우하는 목록
    @OneToMany(mappedBy = "following")
    private List<Follow> followers = new ArrayList<>();
    
    /**
     * Builder
     */
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
	
	/**
	 * Method
	 */
    public void userDelete() {
    	this.status = "DELETED";
    }
    
    public void updateInfo(String name, String username, String email, String phone) {
    	this.name = name;
    	this.username = username;
    	this.email = email;
    	this.phone = phone;
    }
    
    
    public void changePassword(String encodedPwd) {
    	this.password = encodedPwd;
    }
	
	
}
