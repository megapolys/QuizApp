package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.model.quiz.QuizTaskFull;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.property.UploadPathProperty;
import com.example.servingwebcontent.service.quiz.QuizTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizTaskServiceImpl implements QuizTaskService {

	private final QuizPersistence quizPersistence;
	private final UploadPathProperty uploadPathProperty;

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
	@Transactional
	public void deleteQuizTask(Long taskId) {
		QuizTaskFull quizTask = quizPersistence.getQuizTaskFullById(taskId);
		quizPersistence.deleteTaskResultByTaskId(taskId);
		quizPersistence.deleteTaskById(taskId);
		quizPersistence.rePositionTasksByQuizId(quizTask.getQuizId());
		deleteFile(quizTask);
	}

	private void deleteFile(QuizTaskFull task) {
		String fileName;
		if (task.getFiveVariantTask() == null) {
			fileName = task.getYesOrNoTask().getFileName();
		} else {
			fileName = task.getFiveVariantTask().getFileName();
		}
		new File(uploadPathProperty.getImgPrefix() + uploadPathProperty.getImg() + "/" + fileName).delete();
	}
}
