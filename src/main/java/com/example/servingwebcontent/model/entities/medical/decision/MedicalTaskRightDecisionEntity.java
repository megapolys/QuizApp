package com.example.servingwebcontent.model.entities.medical.decision;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "medical_task_right_decisions")
@IdClass(MedicalTaskRightDecisionEntity.class)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class MedicalTaskRightDecisionEntity implements Serializable {

	public static MedicalTaskRightDecisionEntity createNew(Long medicalTaskId, Long decisionId) {
		return new MedicalTaskRightDecisionEntity(medicalTaskId, decisionId);
	}

	@Id
	@Column(name = "medical_task_id")
	Long medicalTaskId;

	@Id
	@Column(name = "right_decisions_id")
	Long decisionsId;
}
