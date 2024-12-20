package com.example.servingwebcontent.model.quiz.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YesOrNoCreateCommandDto {

	Float yesWeight;
	Float noWeight;
}
