package com.example.servingwebcontent.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
public class Role implements GrantedAuthority {

	private long id;

	private String name;

	@Override
	public String getAuthority() {
		return "ROLE_" + name;
	}
}
