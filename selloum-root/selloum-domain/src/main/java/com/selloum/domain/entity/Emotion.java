package com.selloum.domain.entity;

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
@Table(name = "Emotion")

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Emotion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int emtionId;
	
	@Column(nullable = false, unique = false)
	private String emotion;

	@Column(nullable = false, unique = false)
	private String type;
	
	// OneToMany는 추후에 사용 시 주석 해제
//	@OneToMany(mappedBy="emotion")
//	private List<Diary> diary;
	
}
