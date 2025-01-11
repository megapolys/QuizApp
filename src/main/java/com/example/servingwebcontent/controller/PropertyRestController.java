package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.property.ColorRatioProperty;
import com.example.servingwebcontent.property.FiveVariantProperty;
import com.example.servingwebcontent.property.YesOrNoProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PropertyRestController {

	private final FiveVariantProperty fiveVariantProperty;
	private final YesOrNoProperty yesOrNoProperty;
	private final ColorRatioProperty colorRatioProperty;

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

	/**
	 * Получение дефолтных значений для значений цветов в результатах теста
	 *
	 * @return дефолтные значения
	 */
	@GetMapping("api/property/color-ratio")
	ColorRatioProperty getColorRatioProperty() {
		return colorRatioProperty;
	}

}
