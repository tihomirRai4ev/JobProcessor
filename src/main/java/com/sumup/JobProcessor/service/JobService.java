package com.sumup.JobProcessor.service;

import com.sumup.JobProcessor.core.Graph;
import com.sumup.JobProcessor.model.Task;
import com.sumup.JobProcessor.model.Tasks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class JobService {

  public String processJob(Tasks tasks) {
    Graph graph = new Graph();
    Map<String, List<String>> taskToDependencies = new HashMap<>();
    Map<String, Integer> taskToId = new HashMap<>();
    Map<String, Task> taskNameToTask = new HashMap<>();
    int i = 0;
    for (Task task : tasks.getTasks()) {
      graph.addVertex(task.getName()); // Add vertex
      taskToId.put(task.getName(), i++);
      taskNameToTask.put(task.getName(), task);
      String[] requires = task.getRequires();
      if (requires != null) {
        for (String dependency : requires) {
          List<String> dependencies = taskToDependencies.get(task.getName());
          if (dependencies == null) {
            dependencies = new ArrayList<>();
          }
          dependencies.add(dependency);
          taskToDependencies.put(task.getName(), dependencies);
        }
      }
    }

    // Construct the edges in the Graph:
    for (Map.Entry<String, List<String>> taskToDependency : taskToDependencies.entrySet()) {
      int id = taskToId.get(taskToDependency.getKey());
      for (String dep : taskToDependency.getValue()) {
        graph.addEdge(taskToId.get(dep), id);
      }
    }

    graph.performTopologicalSort();
    String[] sortedTasksNames = graph.getSortedArray();
    StringBuilder builder = new StringBuilder();
    builder.append("#!/usr/bin/env bash")
        .append(System.getProperty("line.separator"));

    for (String taskName : sortedTasksNames) {
      if (taskName == null) {
        break;
      }
      builder.append(taskNameToTask.get(taskName).getCommand())
          .append(System.getProperty("line.separator"));
    }

    return builder.toString();
  }
}
