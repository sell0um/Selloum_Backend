package com.selloum.external.ai.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SelloumAiResponseDto {
	
	private String code;
	
	@JsonProperty("final_emotion")
    private String finalEmotion;

    @JsonProperty("top_3_emotions")
    private List<EmotionScoreDto> top3Emotions;

    private Map<String, Double> weights;

}
