package com.example.servingwebcontent.model.quiz;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Quiz {
	Long id;
	String name;
	String shortName;
}
