package com.example.servingwebcontent.model.medical;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MedicalTopic {

	Long id;
	String name;
}
