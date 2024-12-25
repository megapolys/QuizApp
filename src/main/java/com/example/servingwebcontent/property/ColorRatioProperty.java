package com.example.servingwebcontent.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("default.ratio")
public class ColorRatioProperty {

	Float yellow;
	Float red;
}
