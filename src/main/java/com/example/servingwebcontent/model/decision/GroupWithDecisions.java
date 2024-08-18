package com.example.servingwebcontent.model.decision;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupWithDecisions {

	private Long id;

	private String name;

	private List<Decision> decisions;
}
