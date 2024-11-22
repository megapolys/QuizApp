package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.model.quiz.QuizTaskFull;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.service.quiz.QuizTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizTaskServiceImpl implements QuizTaskService {

	private final QuizPersistence quizPersistence;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizTask> getQuizTaskList(Long quizId) {
		return quizPersistence.getQuizTaskList(quizId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuizTask(Long taskId) {
		QuizTaskFull quizTask = quizPersistence.getQuizTaskById(taskId);
		quizPersistence.deleteTaskResultByTaskId(taskId);
		quizPersistence.deleteTaskById(taskId);
		deleteFile(quizTask);
	}


	@Transactional
	public void delete(QuizTask task) {
		for (QuizResultEntity quizResult : quizResultRepository.findAll()) {
			final boolean removed = quizResult.getTaskList()
				.removeIf(resultTask -> Objects.equals(resultTask.getTask().getId(), task.getId()));
			if (removed) {
				quizResultRepository.save(quizResult);
			}
		}
		quizTaskResultRepository.removeAllByTask(task);
		deleteFile(task);
		final Quiz quiz = task.getQuiz();
		for (QuizTask quizTask : quiz.getTaskList()) {
			if (quizTask.getPosition() > task.getPosition()) {
				quizTask.setPosition(quizTask.getPosition() - 1);
				quizTaskRepository.save(quizTask);
			}
		}
		quiz.getTaskList().remove(task);
		quizRepository.save(quiz);
		quizTaskRepository.delete(task);
	}

	private void deleteFile(QuizTaskFull task) {
		final String fileName = task.getFiveVariantTask() != null ? task.getFiveVariantTask().getFileName()
			: task.getYesOrNoTask() != null ? task.getYesOrNoTask().getFileName() : null;
		new File(uploadPathPrefix + uploadPath + "/" + fileName).delete();
	}
}
