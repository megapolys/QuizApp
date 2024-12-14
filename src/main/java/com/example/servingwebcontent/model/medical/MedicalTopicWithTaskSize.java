package com.example.servingwebcontent.model.medical;

import lombok.Value;

// При добавлении полей необходимо дорабатывать метод клонирование MedicalTopicService.clone()
@Value
public class MedicalTopicWithTaskSize {

    Long id;
    String name;
    Long size;
}
