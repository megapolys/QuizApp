package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.converters.quiz.QuizTaskFullEntityToQuizTaskFullConverter;
import com.example.servingwebcontent.exceptions.quiz.QuizNotFoundException;
import com.example.servingwebcontent.exceptions.quiz.QuizResultNotFoundException;
import com.example.servingwebcontent.exceptions.quiz.QuizTaskNotFoundException;
import com.example.servingwebcontent.exceptions.quiz.QuizTaskResultNotFoundException;
import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.result.QuizResultEntity;
import com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultEntity;
import com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultWithTaskTypeEntity;
import com.example.servingwebcontent.model.quiz.*;
import com.example.servingwebcontent.model.quiz.result.QuizResult;
import com.example.servingwebcontent.model.quiz.result.QuizTaskCompleteCommand;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultUpdateCommandDto;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultWithTaskType;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.repositories.DecisionRepository;
import com.example.servingwebcontent.repositories.quiz.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("ConstantConditions")
@Service
@RequiredArgsConstructor
public class QuizPersistenceImpl implements QuizPersistence {

	private final QuizRepository quizRepository;
	private final QuizResultRepository quizResultRepository;
	private final QuizTaskRepository quizTaskRepository;
	private final QuizTaskResultRepository quizTaskResultRepository;
	private final DecisionRepository decisionRepository;
	private final FiveVariantRepository fiveVariantRepository;
	private final YesOrNoRepository yesOrNoRepository;
	private final QuizTaskDecisionsRepository quizTaskDecisionsRepository;

	private final ConversionService conversionService;
	private final QuizTaskFullEntityToQuizTaskFullConverter quizTaskFullConverter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizWithTaskSize> getQuizList() {
		return quizRepository.getQuizListOrderedByShortName().stream()
			.map(quizEntity -> conversionService.convert(quizEntity, QuizWithTaskSize.class))
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Quiz getQuiz(Long id) {
		return quizRepository.findById(id)
			.map(quizEntity -> conversionService.convert(quizEntity, Quiz.class))
			.orElseThrow(() -> QuizNotFoundException.byId(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizTask> getQuizTaskList(Long quizId) {
		return quizTaskRepository.findAllFullByQuizId(quizId).stream()
			.map(quizTaskEntity -> {
				String text;
				if (quizTaskEntity.getFiveVariantTaskEntity() != null) {
					text = quizTaskEntity.getFiveVariantTaskEntity().getQuestionText();
				} else {
					text = quizTaskEntity.getYesOrNoTaskEntity().getQuestionText();
				}
				return QuizTask.builder()
					.id(quizTaskEntity.getId())
					.position(quizTaskEntity.getPosition())
					.text(text)
					.decisionsCount(quizTaskEntity.getCountDecisions())
					.build();
			})
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Quiz findByShortName(String shortName) {
		return quizRepository.findByShortName(shortName)
			.map(quizEntity -> conversionService.convert(quizEntity, Quiz.class))
			.orElse(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addQuiz(QuizCreateCommandDto quiz) {
		quizRepository.save(QuizEntity.createNew(
			quiz.getName(),
			quiz.getShortName()
		));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateQuiz(QuizUpdateCommandDto quiz) {
		quizRepository.save(QuizEntity.buildExisting(
			quiz.getId(),
			quiz.getName(),
			quiz.getShortName()
		));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuizById(Long id) {
		quizRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuizResultByQuizId(Long id) {
		quizResultRepository.deleteAllByQuizId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QuizTaskFull getQuizTaskFullById(Long taskId) {
		return quizTaskRepository.findFullByTaskId(taskId)
			.map(quizTaskFullEntity -> {
				List<Long> allIdsByTaskId = decisionRepository.findAllIdsByTaskId(taskId);
				return quizTaskFullConverter.convert(quizTaskFullEntity, allIdsByTaskId);
			})
			.orElseThrow(() -> QuizTaskNotFoundException.byId(taskId));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTaskResultByTaskId(Long taskId) {
		quizTaskResultRepository.deleteByTaskId(taskId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteTaskById(Long taskId) {
		quizTaskDecisionsRepository.deleteAllByQuizTaskId(taskId);
		quizTaskRepository.deleteById(taskId);
		fiveVariantRepository.deleteById(taskId);
		yesOrNoRepository.deleteById(taskId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rePositionTasksByQuizId(Long quizId) {
		List<QuizTaskEntity> taskList = quizTaskRepository.findAllByQuizId(quizId);
		taskList.sort(Comparator.comparing(QuizTaskEntity::getPosition));
		int position = 1;
		for (QuizTaskEntity quizTaskEntity : taskList) {
			quizTaskEntity.setPosition(position++);
		}
		quizTaskRepository.saveAll(taskList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizResult> getQuizResultListByUserId(Long userId) {
		return quizResultRepository.findAllByUserId(userId).stream()
			.map(entity -> conversionService.convert(entity, QuizResult.class))
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Quiz> getAllQuizzes() {
		return quizRepository.findAll().stream()
			.map(entity -> conversionService.convert(entity, Quiz.class))
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizTaskResultWithTaskType> getQuizTaskResultByQuizResultId(Long id) {
		return quizTaskResultRepository.findAllByQuizResultId(id).stream()
			.map(entity -> conversionService.convert(entity, QuizTaskResultWithTaskType.class))
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteQuizResultById(Long quizResultId) {
		quizTaskResultRepository.deleteAllByQuizResultId(quizResultId);
		quizResultRepository.deleteById(quizResultId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void createNewQuizResult(Long userId, Long quizId) {
		QuizResultEntity savedQuizResult = quizResultRepository.save(QuizResultEntity.createNew(quizId, userId));
		quizTaskRepository.findAllByQuizId(quizId).forEach(quizTaskEntity ->
			quizTaskResultRepository.save(QuizTaskResultEntity.createNew(quizTaskEntity.getId(), savedQuizResult.getId())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean notExistsQuizByUserId(Long userId, Long quizResultId) {
		Optional<QuizResultEntity> quizResultEntity = quizResultRepository.findById(quizResultId);
		return quizResultEntity.isEmpty() || !Objects.equals(quizResultEntity.get().getUserId(), userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean notExistsQuizByUserIdAndTask(Long userId, Long quizTaskResultId) {
		return quizTaskResultRepository.findById(quizTaskResultId)
			.map(quizTaskResultEntity -> notExistsQuizByUserId(userId, quizTaskResultEntity.getQuizResultId()))
			.orElse(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void saveTaskResult(QuizTaskCompleteCommand command) {
		QuizTaskResultEntity quizTaskResultEntity = quizTaskResultRepository.findById(command.getQuizTaskResultId())
			.orElseThrow(() -> QuizTaskResultNotFoundException.byId(command.getQuizTaskResultId()));
		quizTaskResultRepository.save(QuizTaskResultEntity.buildExists(
			quizTaskResultEntity.getId(),
			quizTaskResultEntity.getTaskId(),
			quizTaskResultEntity.getQuizResultId(),
			true,
			command.getVariant(),
			null,
			command.getText()
		));
		if (quizTaskResultRepository.findAllByQuizResultId(quizTaskResultEntity.getQuizResultId()).stream()
			.allMatch(QuizTaskResultWithTaskTypeEntity::isComplete)) {
			QuizResultEntity quizResultEntity = quizResultRepository.findById(quizTaskResultEntity.getQuizResultId())
				.orElseThrow(() -> QuizResultNotFoundException.byId(quizTaskResultEntity.getQuizResultId()));
			quizResultRepository.save(QuizResultEntity.buildExists(
				quizResultEntity.getId(),
				quizResultEntity.getQuizId(),
				quizResultEntity.getUserId(),
				true,
				Instant.now()
			));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Quiz getQuizByQuizResultId(Long quizResultId) {
		QuizResultEntity quizResultEntity = quizResultRepository.findById(quizResultId)
			.orElseThrow(() -> QuizResultNotFoundException.byId(quizResultId));
		return quizRepository.findById(quizResultEntity.getQuizId())
			.map(entity -> conversionService.convert(entity, Quiz.class))
			.orElseThrow(() -> QuizNotFoundException.byId(quizResultEntity.getQuizId()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QuizTaskResultWithTaskType getQuizTaskResultById(Long quizTaskResultId) {
		return quizTaskResultRepository.getQuizTaskResultById(quizTaskResultId)
			.map(entity -> conversionService.convert(entity, QuizTaskResultWithTaskType.class))
			.orElseThrow(() -> QuizTaskResultNotFoundException.byId(quizTaskResultId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateQuizTaskResult(QuizTaskResultUpdateCommandDto command) {
		QuizTaskResultEntity quizTaskResultEntity = quizTaskResultRepository.findById(command.getQuizTaskResultId())
			.orElseThrow(() -> QuizTaskResultNotFoundException.byId(command.getQuizTaskResultId()));
		quizTaskResultRepository.save(QuizTaskResultEntity.buildExists(
			quizTaskResultEntity.getId(),
			quizTaskResultEntity.getTaskId(),
			quizTaskResultEntity.getQuizResultId(),
			quizTaskResultEntity.isComplete(),
			quizTaskResultEntity.getVariant(),
			command.getAltScore(),
			quizTaskResultEntity.getText()
		));
	}
}
