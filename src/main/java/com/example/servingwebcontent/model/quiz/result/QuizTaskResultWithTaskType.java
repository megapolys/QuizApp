package com.example.servingwebcontent.model.quiz.result;

import com.example.servingwebcontent.model.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.model.quiz.task.YesOrNoTask;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class QuizTaskResultWithTaskType {

    /**
     * Идентификатор результата таска
     */
    Long id;

    /**
     * Идентификатор таска
     */
    Long taskId;

    /**
     * Позиция
     */
    Integer position;

    /**
     * true - выполнен
     */
    boolean complete;

    /**
     * Вариант ответа
     */
    String variant;

    /**
     * Значение
     */
    Float score;

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
    FiveVariantTask fiveVariantTask;

    /**
     * Да/нет вопрос
     */
    YesOrNoTask yesOrNoTask;
}
