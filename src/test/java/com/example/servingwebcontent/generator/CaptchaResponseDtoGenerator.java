package com.example.servingwebcontent.generator;

import com.example.servingwebcontent.model.dto.CaptchaResponseDto;

public class CaptchaResponseDtoGenerator {

	public static CaptchaResponseDto generateSuccess() {
		return CaptchaResponseDto.builder()
			.success(true)
			.build();
	}

	public static CaptchaResponseDto generateFailed() {
		return CaptchaResponseDto.builder()
			.success(false)
			.build();
	}
}
