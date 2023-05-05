package com.example.miniproject;

public class Tasks {

    String taskName, taskStatus, taskEstimation, assignedTo;
    public Tasks() {
    }
    public Tasks(String taskName, String taskStatus, String taskEstimation, String assignedTo) {
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskEstimation = taskEstimation;
        this.assignedTo = assignedTo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskEstimation() {
        return taskEstimation;
    }

    public void setTaskEstimation(String taskEstimation) {
        this.taskEstimation = taskEstimation;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
