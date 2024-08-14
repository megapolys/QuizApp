package com.example.servingwebcontent.model.validation;

import com.example.servingwebcontent.model.quiz.decision.QuizDecision;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
//@Scope("session")
public class TaskForm {

    private int taskId;

    private String fileName;
    private boolean deleteFile;

    private int position;
    private String preQuestionText;
    private String questionText;

    private Float firstWeight;
    private Float secondWeight;
    private Float thirdWeight;
    private Float fourthWeight;
    private Float fifthWeight;

    private Float yesWeight;
    private Float noWeight;

    private Set<QuizDecision> decisions;

    private TaskType taskType;

    public void setFromTaskForm(TaskForm taskForm) {
        this.taskId = taskForm.taskId;
        this.position = taskForm.position;
        this.preQuestionText = taskForm.preQuestionText;
        this.questionText = taskForm.questionText;
        this.firstWeight = taskForm.firstWeight;
        this.secondWeight = taskForm.secondWeight;
        this.thirdWeight = taskForm.thirdWeight;
        this.fourthWeight = taskForm.fourthWeight;
        this.fifthWeight = taskForm.fifthWeight;
        this.yesWeight = taskForm.yesWeight;
        this.noWeight = taskForm.noWeight;
        this.decisions = taskForm.decisions;
        this.taskType = taskForm.taskType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isDeleteFile() {
        return deleteFile;
    }

    public void setDeleteFile(boolean deleteFile) {
        this.deleteFile = deleteFile;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPreQuestionText() {
        return preQuestionText;
    }

    public void setPreQuestionText(String preQuestionText) {
        this.preQuestionText = preQuestionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Float getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Float firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Float getSecondWeight() {
        return secondWeight;
    }

    public void setSecondWeight(Float secondWeight) {
        this.secondWeight = secondWeight;
    }

    public Float getThirdWeight() {
        return thirdWeight;
    }

    public void setThirdWeight(Float thirdWeight) {
        this.thirdWeight = thirdWeight;
    }

    public Float getFourthWeight() {
        return fourthWeight;
    }

    public void setFourthWeight(Float fourthWeight) {
        this.fourthWeight = fourthWeight;
    }

    public Float getFifthWeight() {
        return fifthWeight;
    }

    public void setFifthWeight(Float fifthWeight) {
        this.fifthWeight = fifthWeight;
    }

    public Float getYesWeight() {
        return yesWeight;
    }

    public void setYesWeight(Float yesWeight) {
        this.yesWeight = yesWeight;
    }

    public Float getNoWeight() {
        return noWeight;
    }

    public void setNoWeight(Float noWeight) {
        this.noWeight = noWeight;
    }

    public Set<QuizDecision> getDecisions() {
        return decisions;
    }

    public void setDecisions(Set<QuizDecision> decisions) {
        this.decisions = decisions;
    }
}
