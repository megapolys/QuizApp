package com.example.servingwebcontent.service.medical;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.medical.MedicalTask;
import com.example.servingwebcontent.domain.medical.MedicalTopic;
import com.example.servingwebcontent.domain.medical.result.MedicalTopicResult;
import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.repositories.medical.MedicalTaskRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTaskResultRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTopicRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTopicResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class MedicalTopicService {

    private final MedicalTopicRepository medicalTopicRepository;
    private final MedicalTopicResultRepository medicalTopicResultRepository;
    private final MedicalTaskRepository medicalTaskRepository;
    private final MedicalTaskResultRepository medicalTaskResultRepository;

    public MedicalTopicService(MedicalTopicRepository medicalTopicRepository, MedicalTopicResultRepository medicalTopicResultRepository, MedicalTaskRepository medicalTaskRepository, MedicalTaskResultRepository medicalTaskResultRepository) {
        this.medicalTopicRepository = medicalTopicRepository;
        this.medicalTopicResultRepository = medicalTopicResultRepository;
        this.medicalTaskRepository = medicalTaskRepository;
        this.medicalTaskResultRepository = medicalTaskResultRepository;
    }

    public List<MedicalTopic> sortedTopics() {
        return medicalTopicRepository.findAllByOrderByName();
    }

    public void save(MedicalTopic topic) {
        medicalTopicRepository.save(topic);
    }

    @Transactional
    public void delete(MedicalTopic topic) {
        final List<MedicalTopicResult> resultTopicList = medicalTopicResultRepository.findAllByMedicalTopic(topic);
        for (MedicalTopicResult topicResult : resultTopicList) {
            medicalTaskResultRepository.deleteAll(topicResult.getResults());
        }
        medicalTopicResultRepository.deleteAll(resultTopicList);
        medicalTaskRepository.deleteAll(topic.getMedicalTasks());
        medicalTopicRepository.delete(topic);
    }

    public boolean contains(String name) {
        return null != medicalTopicRepository.findByName(name);
    }

    public boolean addTask(MedicalTopic topic, MedicalTask task) {
        if (topic.getMedicalTasks().stream().anyMatch(t -> t.getName().equals(task.getName()))) {
            return false;
        } else {
            task.setName(task.getName().trim());
            topic.getMedicalTasks().add(task);
            medicalTopicRepository.save(topic);
            return true;
        }
    }

    @Transactional
    public void deleteTask(MedicalTopic topic, MedicalTask task) {
        for (MedicalTopicResult result : medicalTopicResultRepository.findAllByMedicalTopic(topic)) {
            if (result.getResults().removeIf(t -> Objects.equals(t.getMedicalTask().getId(), task.getId()))) {
                medicalTopicResultRepository.save(result);
            }
        }
        medicalTaskResultRepository.deleteAllByMedicalTask(task);
        topic.getMedicalTasks().remove(task);
        medicalTopicRepository.save(topic);
        medicalTaskRepository.delete(task);
    }

    public boolean updateTask(MedicalTopic topic, MedicalTask task) {
        if (topic.getMedicalTasks().stream().anyMatch(t -> t.getName().equals(task.getName()) && !Objects.equals(t.getId(), task.getId()))) {
            return false;
        } else {
            medicalTaskRepository.save(task);
            return true;
        }
    }

    public List<TopicBean> getTopics(User user) {
        final Set<MedicalTopicResult> medicalResults = user.getMedicalResults();
        final List<TopicBean> topicBeans = new ArrayList<>();
        for (MedicalTopic topic : medicalTopicRepository.findAllByOrderByName()) {
            boolean inProgress = false;
            boolean exists = false;
            for (MedicalTopicResult result : medicalResults) {
                if (Objects.equals(topic.getId(), result.getMedicalTopic().getId())) {
                    exists = true;
                    if (result.getCompleteDate() == null) {
                        inProgress = true;
                    }
                }
            }
            topicBeans.add(new TopicBean(topic, inProgress, exists));
        }
        return topicBeans;
    }

    public record TopicBean(MedicalTopic topic, boolean inProgress, boolean exists) {}
}
