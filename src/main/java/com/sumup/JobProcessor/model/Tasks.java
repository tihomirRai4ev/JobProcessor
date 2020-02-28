package com.sumup.JobProcessor.model;


public class Tasks {

  private Task[] tasks;

  public Task[] getTasks() {
    return tasks;
  }

  public int getNumberOfTasks() {
    return tasks.length;
  }

  public void setTasks(Task[] tasks) {
    this.tasks = tasks;
  }
}
