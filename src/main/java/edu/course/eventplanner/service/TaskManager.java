package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Task;

import java.util.*;

public class TaskManager {
    private final Queue<Task> upcoming = new LinkedList<>();
    private final Stack<Task> completed = new Stack<>();

    public Task addTask(Task task) {
        upcoming.add(task);
        return task;
    }

    public Task executeNextTask() {
        Task task = upcoming.peek();
        if(task == null) {
            return null;
        }
        upcoming.remove();
        completed.add(task);
        return task;
    }

    public Task undoLastTask() {
        if (completed.isEmpty()) {
            return null;
        }
        Task task = completed.pop();
        upcoming.add(task);
        return task;
    }

    public int remainingTaskCount() {
        return upcoming.size();
    }
}
