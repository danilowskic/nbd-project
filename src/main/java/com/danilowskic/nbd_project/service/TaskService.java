package com.danilowskic.nbd_project.service;

import com.danilowskic.nbd_project.model.Task;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

public interface TaskService {

    /**
     * Add or updates task
     * @param task {@link Task}
     * @param ownerUsername username of the taks's owner
     */
    void saveTask(Task task, String ownerUsername);

    /**
     * Removes task
     * @param id task's id
     * @param ownerUsername username of the taks's owner
     */
    void deleteTask(String id, String ownerUsername);

    /**
     * Fetches task by id
     * @param id task's id
     * @return {@link Task}
     */
    Task getTaskById(String id);

    /**
     * Fetches tasks of owner meeting filters
     * @param ownerUsername owner's username
     * @param filters List of pairs: argument name and filter value
     * @return {@link List<Task>}
     */
    List<Task> searchTasks(String ownerUsername, boolean archive, List<Pair<String, String>> filters);

    /**
     * Generates project-level statistics for the specified owner.
     * Aggregates tasks by grouping them by project name (from flexible attributes)
     * and calculating the total count of tasks per project.
     *
     * @param ownerUsername owner's username
     * @return List of maps, where each map represents a project and its task count
     */
    List<Map> getProjectStats(String ownerUsername);

    /**
     * Calculates average priority of tasks by owner
     * @param ownerUsername owner's username
     * @return average priority
     */
    double getAveragePriority(String ownerUsername);

    /**
     * Sets task to completed
     * @param id
     * @param owner
     */
    void toggleTaskCompletion(String id, String owner);
}
