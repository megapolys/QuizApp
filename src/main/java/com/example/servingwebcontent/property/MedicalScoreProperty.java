package com.example.servingwebcontent.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("default.medical")
public class MedicalScoreProperty {

	Float leftLeft;
	Float leftMid;
	Float rightMid;
	Float rightRight;
}
