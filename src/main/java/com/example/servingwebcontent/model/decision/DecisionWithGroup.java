package com.example.servingwebcontent.model.decision;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DecisionWithGroup {

	Long id;

	String name;

	String description;

	Long groupId;
}
