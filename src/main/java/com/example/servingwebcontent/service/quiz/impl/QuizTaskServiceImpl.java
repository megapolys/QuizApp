package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.exceptions.FileUploadException;
import com.example.servingwebcontent.exceptions.quiz.QuizTaskCreateException;
import com.example.servingwebcontent.exceptions.quiz.QuizTaskUpdateException;
import com.example.servingwebcontent.model.entities.quiz.QuizTaskDecisionsEntity;
import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.task.FiveVariantTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.task.YesOrNoTaskEntity;
import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.model.quiz.QuizTaskFull;
import com.example.servingwebcontent.model.quiz.task.TaskCreateCommandDto;
import com.example.servingwebcontent.model.quiz.task.TaskUpdateCommandDto;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.property.UploadPathProperty;
import com.example.servingwebcontent.repositories.quiz.FiveVariantRepository;
import com.example.servingwebcontent.repositories.quiz.QuizTaskDecisionsRepository;
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
	private final QuizTaskDecisionsRepository quizTaskDecisionsRepository;

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
	public QuizTaskFull getQuizTaskById(Long taskId) {
		return quizPersistence.getQuizTaskFullById(taskId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteQuizTask(Long taskId) {
		deleteQuizTask(taskId, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteAllQuizTask(Long quizId) {
		quizTaskRepository.findAllByQuizId(quizId)
			.forEach(quizTask -> deleteQuizTask(quizTask.getId(), false));
	}

	private void deleteQuizTask(Long taskId, boolean reposition) {
		QuizTaskFull quizTask = quizPersistence.getQuizTaskFullById(taskId);
		quizPersistence.deleteTaskResultByTaskId(taskId);
		quizPersistence.deleteTaskById(taskId);
		if (reposition) {
			quizPersistence.rePositionTasksByQuizId(quizTask.getQuizId());
		}
		deleteFile(quizTask);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void createTask(TaskCreateCommandDto command, MultipartFile file) {
		if (command.getFiveVariant() != null && command.getYesOrNo() != null) {
			throw QuizTaskCreateException.byVariants();
		}
		String fileName = null;
		if (file != null && !file.isEmpty()) {
			fileName = UUID.randomUUID() + "." + file.getOriginalFilename();
		}
		movePosition(command.getQuizId(), command.getPosition());
		QuizTaskEntity taskEntity = quizTaskRepository.save(QuizTaskEntity.createNew(
			command.getQuizId(),
			command.getPosition()
		));
		if (command.getFiveVariant() != null) {
			fiveVariantRepository.save(
				FiveVariantTaskEntity.createNew(
					taskEntity.getId(),
					command.getPreQuestionText(),
					command.getQuestionText(),
					fileName,
					command.getFiveVariant().getFirstWeight(),
					command.getFiveVariant().getSecondWeight(),
					command.getFiveVariant().getThirdWeight(),
					command.getFiveVariant().getFourthWeight(),
					command.getFiveVariant().getFifthWeight()
				));
		}
		if (command.getYesOrNo() != null) {
			yesOrNoRepository.save(
				YesOrNoTaskEntity.createNew(
					taskEntity.getId(),
					command.getPreQuestionText(),
					command.getQuestionText(),
					fileName,
					command.getYesOrNo().getYesWeight(),
					command.getYesOrNo().getNoWeight()
				));
		}
		if (command.getDecisionIds() != null) {
			for (Long decisionId : command.getDecisionIds()) {
				quizTaskDecisionsRepository.save(QuizTaskDecisionsEntity.createNew(taskEntity.getId(), decisionId));
			}
		}
		if (fileName != null) {
			saveFile(file, fileName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void updateTask(TaskUpdateCommandDto command, MultipartFile file, Long taskId) {
		if (command.getFiveVariant() != null && command.getYesOrNo() != null) {
			throw QuizTaskUpdateException.byVariants();
		}
		QuizTaskFull task = quizPersistence.getQuizTaskFullById(taskId);
		String fileName = null;
		if (file != null && !file.isEmpty()) {
			fileName = UUID.randomUUID() + "." + file.getOriginalFilename();
		}
		movePosition(task.getQuizId(), command.getPosition());
		quizTaskRepository.save(QuizTaskEntity.buildExists(
			task.getId(),
			task.getQuizId(),
			command.getPosition()
		));
		if (command.getFiveVariant() != null) {
			if (task.getYesOrNoTask() != null) {
				yesOrNoRepository.deleteById(task.getYesOrNoTask().getId());
				fiveVariantRepository.save(
					FiveVariantTaskEntity.createNew(
						taskId,
						command.getPreQuestionText(),
						command.getQuestionText(),
						command.isDeleteFile() ? null :
							fileName == null ? task.getYesOrNoTask().getFileName() : fileName,
						command.getFiveVariant().getFirstWeight(),
						command.getFiveVariant().getSecondWeight(),
						command.getFiveVariant().getThirdWeight(),
						command.getFiveVariant().getFourthWeight(),
						command.getFiveVariant().getFifthWeight()
					));
			} else {
				fiveVariantRepository.save(
					FiveVariantTaskEntity.buildExists(
						taskId,
						command.getPreQuestionText(),
						command.getQuestionText(),
						command.isDeleteFile() ? null :
							fileName == null ? task.getFiveVariantTask().getFileName() : fileName,
						command.getFiveVariant().getFirstWeight(),
						command.getFiveVariant().getSecondWeight(),
						command.getFiveVariant().getThirdWeight(),
						command.getFiveVariant().getFourthWeight(),
						command.getFiveVariant().getFifthWeight()
					));
			}
		}
		if (command.getYesOrNo() != null) {
			if (task.getFiveVariantTask() != null) {
				fiveVariantRepository.deleteById(task.getFiveVariantTask().getId());
				yesOrNoRepository.save(
					YesOrNoTaskEntity.createNew(
						taskId,
						command.getPreQuestionText(),
						command.getQuestionText(),
						command.isDeleteFile() ? null :
							fileName == null ? task.getFiveVariantTask().getFileName() : fileName,
						command.getYesOrNo().getYesWeight(),
						command.getYesOrNo().getNoWeight()
					));
			} else {
				yesOrNoRepository.save(
					YesOrNoTaskEntity.buildExists(
						taskId,
						command.getPreQuestionText(),
						command.getQuestionText(),
						command.isDeleteFile() ? null :
							fileName == null ? task.getYesOrNoTask().getFileName() : fileName,
						command.getYesOrNo().getYesWeight(),
						command.getYesOrNo().getNoWeight()
					));
			}
		}
		if (command.getDecisionIds() != null) {
			for (Long decisionId : command.getDecisionIds()) {
				quizTaskDecisionsRepository.save(QuizTaskDecisionsEntity.createNew(task.getId(), decisionId));
			}
		}
		if (command.isDeleteFile()) {
			deleteFile(task);
		}
		if (fileName != null) {
			deleteFile(task);
			saveFile(file, fileName);
		}
	}

	private void saveFile(MultipartFile multipartFile, String fileName) {
		try {
			final File uploadDir = new File(uploadPathProperty.getImgPrefix() + uploadPathProperty.getImg());
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			File file = new File(uploadDir + "/" + fileName);
			multipartFile.transferTo(file);
		} catch (IOException ioe) {
			throw FileUploadException.quizTaskFile(ioe);
		}
	}

	private void deleteFile(QuizTaskFull task) {
		String fileName;
		if (task.getFiveVariantTask() == null) {
			fileName = task.getYesOrNoTask().getFileName();
		} else {
			fileName = task.getFiveVariantTask().getFileName();
		}
		if (fileName != null) {
			new File(uploadPathProperty.getImgPrefix() + uploadPathProperty.getImg() + "/" + fileName).delete();
		}
	}

	private void movePosition(Long quizId, Integer position) {
		if (quizTaskRepository.existsByQuizIdAndPosition(quizId, position)) {
			quizTaskRepository.updateByQuizIdAndPositionGatherOrEquals(quizId, position);
		}
	}
}
