package com.example.servingwebcontent.service.medical.impl;

import com.example.servingwebcontent.model.medical.result.MedicalTopicResult;
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

	public boolean userNotContainsQuiz(Long userId, Long topicResultId) {
//        final User user = userRepository.findById(userId).orElseThrow(); // нужно для актуализации данных из бд
//        return user.getMedicalResults().stream().noneMatch(r -> r.getId().equals(topicResultId));
		return false;
	}

	public void save(MedicalTopicResult topicResult) {
//        final Date curDate = new Date();
//        if (topicResult.getCompleteDate() == null) {
//            topicResult.setCompleteDate(curDate);
//        }
//        topicResult.setLastUpdateDate(curDate);
//        topicResultRepository.save(topicResult);
	}

	public record TopicResultBean(String name, boolean inProgress, boolean complete, Long topicResultId,
								  String progress, Date completeDate, Date lastUpdateDate) {
	}

}
