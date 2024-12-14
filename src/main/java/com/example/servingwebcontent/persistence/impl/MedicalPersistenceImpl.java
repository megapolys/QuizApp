package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.model.entities.medical.MedicalTopicEntity;
import com.example.servingwebcontent.model.medical.MedicalTopicCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;
import com.example.servingwebcontent.persistence.MedicalPersistence;
import com.example.servingwebcontent.repositories.medical.MedicalTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ConstantConditions")
@Service
@RequiredArgsConstructor
public class MedicalPersistenceImpl implements MedicalPersistence {

	private final MedicalTopicRepository medicalTopicRepository;
	private final ConversionService conversionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MedicalTopicWithTaskSize> getMedicalTopicList() {
		return medicalTopicRepository.findAllByOrderByName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByName(String name) {
		return medicalTopicRepository.existsByName(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createMedicalTopic(MedicalTopicCreateCommandDto command) {
		medicalTopicRepository.save(MedicalTopicEntity.createNew(command.getName()));
	}
}
