package com.example.servingwebcontent.model.medical.result;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalTaskResultDto {

	@NotNull
	Long taskResultId;

	Float value;
}
