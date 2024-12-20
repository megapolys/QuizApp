package com.example.servingwebcontent.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Password {

	String password;

	String password2;
}
