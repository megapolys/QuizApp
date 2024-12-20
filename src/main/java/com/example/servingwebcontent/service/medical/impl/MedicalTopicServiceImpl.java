package com.example.servingwebcontent.service.medical.impl;

import com.example.servingwebcontent.exceptions.medical.MedicalTopicAlreadyExistsException;
import com.example.servingwebcontent.model.medical.MedicalTopic;
import com.example.servingwebcontent.model.medical.MedicalTopicCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTopicUpdateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;
import com.example.servingwebcontent.model.user.User;
import com.example.servingwebcontent.persistence.MedicalPersistence;
import com.example.servingwebcontent.service.medical.MedicalTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalTopicServiceImpl implements MedicalTopicService {

    private final MedicalPersistence medicalPersistence;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MedicalTopicWithTaskSize> getMedicalTopicList() {
        return medicalPersistence.getMedicalTopicList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MedicalTopic getMedicalTopic(Long id) {
        return medicalPersistence.getMedicalTopic(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMedicalTopic(MedicalTopicCreateCommandDto command) {
        if (medicalPersistence.existsByName(command.getName())) {
            throw MedicalTopicAlreadyExistsException.byName(command.getName());
        }
        medicalPersistence.createMedicalTopic(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMedicalTopic(MedicalTopicUpdateCommandDto command) {
        MedicalTopic topicByName = medicalPersistence.findByName(command.getName());
        if (topicByName != null && !topicByName.getId().equals(command.getId())) {
            throw MedicalTopicAlreadyExistsException.byName(command.getName());
        }
        medicalPersistence.updateMedicalTopic(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMedicalTopic(Long id) {
        medicalPersistence.deleteMedicalTopic(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cloneMedicalTopic(Long id) {
        MedicalTopic medicalTopic = medicalPersistence.getMedicalTopic(id);
        int i = 1;
        String finalName;
        while (true) {
            final String name = medicalTopic.getName() + " - копия " + i;
            if (medicalPersistence.existsByName(name)) {
                i++;
            } else {
                finalName = name;
                break;
            }
        }
        medicalPersistence.cloneMedicalTopic(id, finalName);
    }

    // Пока не нашел, где нужно, но проверяются topicResult
    public List<TopicBean> getTopics(User user) {
//        final Set<MedicalTopicResult> medicalResults = user.getMedicalResults();
//        final List<TopicBean> topicBeans = new ArrayList<>();
//        for (MedicalTopic topic : medicalTopicRepository.findAllByOrderByName()) {
//            boolean inProgress = false;
//            boolean exists = false;
//            for (MedicalTopicResult result : medicalResults) {
//                if (Objects.equals(topic.getId(), result.getMedicalTopic().getId())) {
//                    exists = true;
//                    if (result.getCompleteDate() == null) {
//                        inProgress = true;
//                    }
//                }
//            }
//            topicBeans.add(new TopicBean(topic, inProgress, exists));
//        }
//        return topicBeans;
        return null;
    }

    public record TopicBean(MedicalTopicWithTaskSize topic, boolean inProgress, boolean exists) {

    }
}
