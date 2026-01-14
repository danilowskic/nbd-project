package com.danilowskic.nbd_project.service;

import com.danilowskic.nbd_project.model.Task;
import org.springframework.context.annotation.Primary;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Primary
public class TaskServiceImpl implements TaskService {

    private final TaskCommandService commandService;
    private final TaskQueryService queryService;
    private final TaskStatisticsService statsService;

    public TaskServiceImpl(TaskCommandService commandService, TaskQueryService queryService, TaskStatisticsService statsService) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.statsService = statsService;
    }

    @Override
    public void saveTask(Task task, String ownerUsername) {
        commandService.save(task, ownerUsername);
    }

    @Override
    public void deleteTask(String id, String ownerUsername) {
        commandService.delete(id, ownerUsername);
    }

    @Override
    public Task getTaskById(String id) {
        return queryService.getById(id);
    }

    @Override
    public List<Task> searchTasks(String owner, List<Pair<String, String>> filters) {
        return queryService.search(owner, filters);
    }

    @Override
    public List<Map> getProjectStats(String owner) {
        return statsService.getStatsByProject(owner);
    }

    @Override
    public double getAveragePriority(String owner) {
        return statsService.getAveragePriority(owner);
    }
}