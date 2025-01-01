package com.example.servingwebcontent.model.entities.quiz.result;

import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.task.FiveVariantTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.task.YesOrNoTaskEntity;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QuizTaskResultWithTaskTypeEntity {

    /**
     * Идентификатор результата таска
     */
    Long id;

    /**
     * Таск
     */
    QuizTaskEntity taskEntity;

    /**
     * true - выполнен
     */
    boolean complete;

    /**
     * Вариант ответа
     */
    String variant;

    /**
     * Альтернативное значение
     */
    Float altScore;

    /**
     * Текст ответа (если есть)
     */
    String text;

    /**
     * Вопрос в пять вариантов
     */
    FiveVariantTaskEntity fiveVariantTaskEntity;

    /**
     * Да/нет вопрос
     */
    YesOrNoTaskEntity yesOrNoTaskEntity;
}
