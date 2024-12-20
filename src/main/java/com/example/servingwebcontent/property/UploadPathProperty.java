package com.example.servingwebcontent.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("upload.path")
public class UploadPathProperty {

	String img;

	String imgPrefix;
}
