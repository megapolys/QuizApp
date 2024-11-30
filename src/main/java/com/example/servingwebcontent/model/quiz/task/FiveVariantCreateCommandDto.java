package com.example.servingwebcontent.model.quiz.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FiveVariantCreateCommandDto {

	Float firstWeight;
	Float secondWeight;
	Float thirdWeight;
	Float fourthWeight;
	Float fifthWeight;
}
