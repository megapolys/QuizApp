package com.example.servingwebcontent.persistence;

import com.example.servingwebcontent.model.quiz.*;
import com.example.servingwebcontent.model.quiz.result.QuizResult;
import com.example.servingwebcontent.model.quiz.result.QuizTaskCompleteCommand;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultUpdateCommandDto;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultWithTaskType;

import java.util.List;

public interface QuizPersistence {

	/**
	 * Получение списка тестов, сортированных по shortName
	 *
	 * @return Список тестов
	 */
	List<QuizWithTaskSize> getQuizList();

	/**
	 * Получение теста по идентификатору
	 *
	 * @param id - Идентификатор теста
	 *
	 * @return Тест
	 */
	Quiz getQuiz(Long id);

	/**
	 * Получить список вопросов
	 *
	 * @param quizId идентификатор теста
	 *
	 * @return список вопросов
	 */
	List<QuizTask> getQuizTaskList(Long quizId);

	/**
	 * Получение теста по короткому наименованию
	 *
	 * @return Тест
	 */
	Quiz findByShortName(String shortName);

	/**
	 * Добавить новый тест
	 *
	 * @param quiz - Тест
	 */
	void addQuiz(QuizCreateCommandDto quiz);

	/**
	 * Изменение данных теста
	 *
	 * @param quiz - Тест
	 */
	void updateQuiz(QuizUpdateCommandDto quiz);

	/**
	 * Удаление теста
	 *
	 * @param id - Идентификатор теста
	 */
	void deleteQuizById(Long id);

	/**
	 * Удалить все выполненные тесты по идентификатору теста
	 *
	 * @param id - Идентификатор теста
	 */
	void deleteQuizResultByQuizId(Long id);

	/**
	 * Получить вопрос целиком
	 *
	 * @param taskId идентификатор вопроса
	 *
	 * @return вопрос
	 */
	QuizTaskFull getQuizTaskFullById(Long taskId);

	/**
	 * Удаление всех ответов на вопрос по идентификатору вопроса
	 *
	 * @param taskId идентификатор вопроса
	 */
	void deleteTaskResultByTaskId(Long taskId);

	/**
	 * Удаление вопроса по идентификатору
	 *
	 * @param taskId идентификатор вопроса
	 */
	void deleteTaskById(Long taskId);

	/**
	 * Выровнять позиции для вопросов теста
	 *
	 * @param quizId идентификатор теста
	 */
	void rePositionTasksByQuizId(Long quizId);

	/**
	 * Получить список результатов тестов
	 *
	 * @param userId идентификатор профиля
	 */
	List<QuizResult> getQuizResultListByUserId(Long userId);

	/**
	 * Получение всех тестов
	 *
	 * @return тесты
	 */
	List<Quiz> getAllQuizzes();

	/**
	 * Получить список результатов тасков в тесте
	 *
	 * @param id идентификатор результата теста
	 *
	 * @return список результатов тасков
	 */
	List<QuizTaskResultWithTaskType> getQuizTaskResultByQuizResultId(Long id);

	/**
	 * Удаление результата теста по идентификатору
	 *
	 * @param quizResultId идентификатор результата теста
	 */
	void deleteQuizResultById(Long quizResultId);

	/**
	 * Назначение пользователю теста на выполнение (создание нового результата теста)
	 *
	 * @param userId идентификатор пользователя, которому назначается тест
	 * @param quizId идентификатор теста
	 */
	void createNewQuizResult(Long userId, Long quizId);

	/**
	 * Проверка, есть ли у пользователя результат теста с данным идентификатором
	 *
	 * @param userId       идентификатор пользователя
	 * @param quizResultId идентификатор результата теста
	 *
	 * @return true - если есть тест назначен
	 */
	boolean notExistsQuizByUserId(Long userId, Long quizResultId);

	/**
	 * Проверка, есть ли у пользователя результат теста с данным идентификатором результата вопроса
	 *
	 * @param userId           идентификатор пользователя
	 * @param quizTaskResultId идентификатор результата вопроса
	 *
	 * @return true - если есть тест назначен
	 */
	boolean notExistsQuizByUserIdAndTask(Long userId, Long quizTaskResultId);

	/**
	 * Сохранить результат ответа
	 *
	 * @param command команда с результатом ответа
	 */
	void saveTaskResult(QuizTaskCompleteCommand command);

	/**
	 * Получить тест по идентификатору результата теста
	 *
	 * @param quizResultId идентификатор результата теста
	 *
	 * @return тест
	 */
	Quiz getQuizByQuizResultId(Long quizResultId);

	/**
	 * Получение результата теста
	 *
	 * @param quizTaskResultId идентификатор результата теста
	 *
	 * @return результат теста
	 */
	QuizTaskResultWithTaskType getQuizTaskResultById(Long quizTaskResultId);

	/**
	 * Изменить значение для результата вопроса
	 *
	 * @param command команда для обновления
	 */
	void updateQuizTaskResult(QuizTaskResultUpdateCommandDto command);
}
