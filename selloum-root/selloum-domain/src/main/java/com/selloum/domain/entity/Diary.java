package com.selloum.domain.entity;

import java.util.List;

import com.selloum.domain.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@Table(name = "diary")
public class Diary extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long diaryId;

	private String diaryFilePath;
	
	private String content;

	private boolean isAnalyzed = false;
	
	private boolean isPublic;
	
	private boolean complaint;
	
	
	// FK - Emotion / User
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emotion_id")
	private Emotion emotion;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
//	@OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
//	private List<Reaction> reaction;
	

    
	
}
