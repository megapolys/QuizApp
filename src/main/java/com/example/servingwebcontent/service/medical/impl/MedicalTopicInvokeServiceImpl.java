package com.example.servingwebcontent.service.medical.impl;

import com.example.servingwebcontent.exceptions.AccessDeniedException;
import com.example.servingwebcontent.model.medical.MedicalTopic;
import com.example.servingwebcontent.model.medical.result.MedicalInvokeTopicDto;
import com.example.servingwebcontent.model.medical.result.MedicalTaskResultWithTask;
import com.example.servingwebcontent.model.medical.result.MedicalTopicResultUpdateCommandDto;
import com.example.servingwebcontent.persistence.MedicalPersistence;
import com.example.servingwebcontent.service.medical.MedicalTopicInvokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalTopicInvokeServiceImpl implements MedicalTopicInvokeService {

	private final MedicalPersistence medicalPersistence;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createNewMedicalResult(Long userId, Long topicId) {
		medicalPersistence.createNewMedicalResult(userId, topicId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MedicalInvokeTopicDto getMedicalTopicResult(Long topicResultId, Long userId) {
		if (medicalPersistence.notExistsMedicalTopicResultByUserId(userId, topicResultId)) {
			throw AccessDeniedException.medicalByUserId(userId, topicResultId);
		}
		MedicalTopic medicalTopic = medicalPersistence.getMedicalTopicByTopicResultId(topicResultId);
		List<MedicalTaskResultWithTask> results = medicalPersistence.getMedicalTaskResultByTopicResultId(topicResultId);
		return MedicalInvokeTopicDto.builder()
			.topicName(medicalTopic.getName())
			.results(results)
			.build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveMedicalTopicResult(MedicalTopicResultUpdateCommandDto command, Long userId) {
		if (medicalPersistence.notExistsMedicalTopicResultByUserId(userId, command.getTopicResultId())) {
			throw AccessDeniedException.medicalByUserId(userId, command.getTopicResultId());
		}
		medicalPersistence.saveMedicalTopicResult(command);
	}

	public List<TopicResultBean> getTopicResults(Long userId) {
//        final User user = userRepository.findById(userId).orElseThrow(); // нужно для актуализации данных из бд
//        return user.getMedicalResults().stream()
//            .sorted(Comparator.comparing(MedicalTopicResult::getLastUpdateDate, Comparator.nullsFirst(Comparator.reverseOrder())))
//            .map(result -> {
//                final boolean inProgress = result.getResults().stream().anyMatch(task -> task.getValue() != null);
//                return new TopicResultBean(
//                    result.getMedicalTopic().getName(),
//                    inProgress,
//                    result.getCompleteDate() != null,
//                    result.getId(),
//                    getProgress(result),
//                    result.getCompleteDate(),
//                    result.getLastUpdateDate()
//                );
//            }).toList();
		return null;
	}

	public record TopicResultBean(String name, boolean inProgress, boolean complete, Long topicResultId,
								  String progress, Date completeDate, Date lastUpdateDate) {
	}

}
