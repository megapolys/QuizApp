package com.example.servingwebcontent.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("default.five-variant")
public class FiveVariantProperty {

	Float firstWeight;
	Float secondWeight;
	Float thirdWeight;
	Float fourthWeight;
	Float fifthWeight;
}
