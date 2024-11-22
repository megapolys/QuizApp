package com.example.servingwebcontent.model.quiz;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.entities.quiz.task.FiveVariantTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.task.YesOrNoTaskEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizTaskFull {

	/**
	 * Идентификатор вопроса
	 */
	Long id;

	/**
	 * Порядковый номер отображения в интерфейсе
	 */
	int position;

	/**
	 * Текст вопроса
	 */
	String text;

	/**
	 * Список решений
	 */
	List<Decision> decisions;

	/**
	 * Вопрос в пять вариантов
	 */
	FiveVariantTaskEntity fiveVariantTaskEntity;

	/**
	 * Да/нет вопрос
	 */
	YesOrNoTaskEntity yesOrNoTaskEntity;
}
