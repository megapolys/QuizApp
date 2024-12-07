package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.exceptions.FileUploadException;
import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.task.FiveVariantTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.task.YesOrNoTaskEntity;
import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.model.quiz.QuizTaskFull;
import com.example.servingwebcontent.model.quiz.task.TaskCreateCommandDto;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.property.UploadPathProperty;
import com.example.servingwebcontent.repositories.quiz.FiveVariantRepository;
import com.example.servingwebcontent.repositories.quiz.QuizTaskRepository;
import com.example.servingwebcontent.repositories.quiz.YesOrNoRepository;
import com.example.servingwebcontent.service.quiz.QuizTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizTaskServiceImpl implements QuizTaskService {

	private final QuizPersistence quizPersistence;
	private final UploadPathProperty uploadPathProperty;
	private final QuizTaskRepository quizTaskRepository;
	private final FiveVariantRepository fiveVariantRepository;
	private final YesOrNoRepository yesOrNoRepository;

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createTask(TaskCreateCommandDto taskCreateCommand, MultipartFile file) {
		String fileName = saveFileIfNotEmpty(file);
		FiveVariantTaskEntity fiveVariantEntity = null;
		YesOrNoTaskEntity yesOrNoEntity = null;
		if (taskCreateCommand.getFiveVariant() != null) {
			fiveVariantEntity = fiveVariantRepository.save(
				FiveVariantTaskEntity.createNew(
					taskCreateCommand.getPreQuestionText(),
					taskCreateCommand.getQuestionText(),
					fileName,
					taskCreateCommand.getFiveVariant().getFirstWeight(),
					taskCreateCommand.getFiveVariant().getSecondWeight(),
					taskCreateCommand.getFiveVariant().getThirdWeight(),
					taskCreateCommand.getFiveVariant().getFourthWeight(),
					taskCreateCommand.getFiveVariant().getFifthWeight()
				));
		}
		if (taskCreateCommand.getYesOrNo() != null) {
			yesOrNoEntity = yesOrNoRepository.save(
				YesOrNoTaskEntity.createNew(
					taskCreateCommand.getPreQuestionText(),
					taskCreateCommand.getQuestionText(),
					fileName,
					taskCreateCommand.getYesOrNo().getYesWeight(),
					taskCreateCommand.getYesOrNo().getNoWeight()
				));
		}
		movePosition(taskCreateCommand.getQuizId(), taskCreateCommand.getPosition());
		quizTaskRepository.save(QuizTaskEntity.createNew(
			taskCreateCommand.getQuizId(),
			taskCreateCommand.getPosition(),
			fiveVariantEntity == null ? null : fiveVariantEntity.getId(),
			yesOrNoEntity == null ? null : yesOrNoEntity.getId()
		));
	}

	private String saveFileIfNotEmpty(MultipartFile file) {
		String fileName = null;
		try {
			if (file != null && !file.isEmpty()) {
				final File uploadDir = new File(uploadPathProperty.getImgPrefix() + uploadPathProperty.getImg());
				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}
				final String uuid = UUID.randomUUID().toString();
				fileName = uuid + "." + file.getOriginalFilename();
				file.transferTo(new File(uploadDir + "/" + fileName));
			}
		} catch (IOException ioe) {
			throw FileUploadException.quizTaskFile(ioe);
		}
		return fileName;
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

	private void movePosition(Long quizId, Integer position) {
		if (quizTaskRepository.existsByQuizIdAndPosition(quizId, position)) {
			quizTaskRepository.updateByQuizIdAndPositionGatherOrEquals(quizId, position);
		}
	}
}
