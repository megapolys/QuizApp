package com.example.servingwebcontent.service.medical;

import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;
import com.example.servingwebcontent.model.medical.result.MedicalTopicResult;
import com.example.servingwebcontent.repositories.UserRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTopicResultRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MedicalTopicInvokeService {

    private final UserRepository userRepository;
    private final MedicalTopicResultRepository topicResultRepository;

    public MedicalTopicInvokeService(UserRepository userRepository, MedicalTopicResultRepository topicResultRepository) {
        this.userRepository = userRepository;
        this.topicResultRepository = topicResultRepository;
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

    public String getProgress(MedicalTopicResult result) {
        final long countCompleted = result.getResults().stream().filter(task -> task.getValue() != null).count();
        final int taskCount = result.getResults().size();
        return countCompleted + "/" + taskCount;
    }

	public void startTopic(Long userId, MedicalTopicWithTaskSize topic) {
//        final User user = userRepository.findById(userId).orElseThrow(); // нужно для актуализации данных из бд
//        final MedicalTopicResult topicResult = new MedicalTopicResult();
//        topicResult.setMedicalTopic(topic);
//        final Set<MedicalTaskResult> topicTaskResults = topic.getMedicalTasks().stream().map(task -> {
//            final MedicalTaskResult taskResult = new MedicalTaskResult();
//            taskResult.setMedicalTask(task);
//            taskResult.setTopicResult(topicResult);
//            return taskResult;
//        }).collect(Collectors.toSet());
//        topicResult.setResults(topicTaskResults);
//        topicResult.setUser(user);
//        topicResultRepository.save(topicResult);
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

    public record TopicResultBean(String name, boolean inProgress, boolean complete, Long topicResultId, String progress, Date completeDate, Date lastUpdateDate) {}

}
