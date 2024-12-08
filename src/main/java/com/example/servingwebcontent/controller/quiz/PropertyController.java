package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.property.FiveVariantProperty;
import com.example.servingwebcontent.property.YesOrNoProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PropertyController {

	private final FiveVariantProperty fiveVariantProperty;
	private final YesOrNoProperty yesOrNoProperty;

	/**
	 * Получение дефолтных значений для вопроса с 5 вариантами
	 *
	 * @return дефолтные значения
	 */
	@GetMapping("api/property/fiveVariant")
	FiveVariantProperty getFiveVariantProperty() {
		return fiveVariantProperty;
	}

	/**
	 * Получение дефолтных значений для вопроса вариантами да/нет
	 *
	 * @return дефолтные значения
	 */
	@GetMapping("api/property/yesOrNo")
	YesOrNoProperty getYesOrNoProperty() {
		return yesOrNoProperty;
	}

}
