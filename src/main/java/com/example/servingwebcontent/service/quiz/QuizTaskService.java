package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.model.quiz.QuizTaskFull;
import com.example.servingwebcontent.model.quiz.task.TaskCreateCommandDto;
import com.example.servingwebcontent.model.quiz.task.TaskUpdateCommandDto;
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
	 * Получить данные вопроса
	 *
	 * @param taskId идентификатор вопроса
	 *
	 * @return вопрос
	 */
	QuizTaskFull getQuizTaskById(Long taskId);

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

	/**
	 * Изменение вопроса
	 *
	 * @param taskUpdateCommand команда для изменения вопроса
	 * @param file              файл
	 * @param taskId            идентификатор вопроса
	 */
	void updateTask(TaskUpdateCommandDto taskUpdateCommand, MultipartFile file, Long taskId);
}
