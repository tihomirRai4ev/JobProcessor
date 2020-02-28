package com.sumup.JobProcessor.model;

public class Task {

  private String name;
  private String command;
  private String[] requires;

  public String[] getRequires() {
    return requires;
  }

  public void setRequires(String[] requires) {
    this.requires = requires;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }
}
