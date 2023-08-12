package com.example.todoapp.Model;

public class TaskModel {
    private int id, status, fragmentId;
    private String task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getFragment_id() {
        return fragmentId;
    }

    public void setFragment_id(int fragment_id) {
        this.fragmentId = fragment_id;
    }
}
