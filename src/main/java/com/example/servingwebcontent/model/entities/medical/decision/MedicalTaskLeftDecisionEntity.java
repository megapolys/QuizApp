package com.example.servingwebcontent.model.entities.medical.decision;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "medical_task_left_decisions")
@IdClass(MedicalTaskLeftDecisionEntity.class)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class MedicalTaskLeftDecisionEntity implements Serializable {

	public static MedicalTaskLeftDecisionEntity createNew(Long medicalTaskId, Long decisionId) {
		return new MedicalTaskLeftDecisionEntity(medicalTaskId, decisionId);
	}

	@Id
	@Column(name = "medical_task_id")
	Long medicalTaskId;

	@Id
	@Column(name = "left_decisions_id")
	Long decisionsId;
}
