package com.example.servingwebcontent.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("default.yes-or-no")
public class YesOrNoProperty {

	Float noWeight;
	Float yesWeight;
}
