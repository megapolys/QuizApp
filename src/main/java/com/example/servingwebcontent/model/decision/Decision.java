package com.example.servingwebcontent.model.decision;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Decision {

	private Long id;

	private String name;

	private String description;
}
