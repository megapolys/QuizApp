package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.model.quiz.task.TaskCreateCommandDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuizTaskService {

	/**
	 * Получить список вопросов
	 *
	 * @param quizId идентификатор теста
	 *
	 * @return список вопросов
	 */
	List<QuizTask> getQuizTaskList(Long quizId);

	/**
	 * Удаление вопроса по идентификатору
	 *
	 * @param taskId идентификатор вопроса
	 */
	void deleteQuizTask(Long taskId);

	/**
	 * Создать вопрос
	 *
	 * @param taskCreateCommand команда создания вопроса
	 * @param file              файл
	 */
	void createTask(TaskCreateCommandDto taskCreateCommand, MultipartFile file);
}
