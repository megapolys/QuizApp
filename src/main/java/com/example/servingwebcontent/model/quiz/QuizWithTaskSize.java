package com.example.servingwebcontent.model.quiz;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizWithTaskSize {
	Long id;
	String name;
	String shortName;
	long size;
}
