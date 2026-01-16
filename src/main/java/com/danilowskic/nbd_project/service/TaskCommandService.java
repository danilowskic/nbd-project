package com.danilowskic.nbd_project.service;

import com.danilowskic.nbd_project.exception.ForbiddenActionException;
import com.danilowskic.nbd_project.exception.TaskNotFoundException;
import com.danilowskic.nbd_project.model.Task;
import com.danilowskic.nbd_project.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class TaskCommandService {

    private final TaskRepository repository;

    public TaskCommandService(TaskRepository repository) {
        this.repository = repository;
    }

    public void save(Task task, String ownerUsername) {
        if (task.getId().isEmpty()) {
            log.info("Task is being created for user {}", ownerUsername);
            task.setId(null);
        }

        if (task.getId() == null) {
            task.setUser(ownerUsername);
        } else {
            log.info("Task is being updated for user {}", ownerUsername);

            Task existingTask = repository.findById(task.getId())
                    .orElseThrow(() -> new TaskNotFoundException(task.getId()));

            if (!existingTask.getUser().equals(ownerUsername)) {
                throw new ForbiddenActionException("This task cannot be edited");
            }

            task.setUser(existingTask.getUser());
        }

        Task savedTask = repository.save(task);

        log.info("Created task with ID {}", savedTask.getId());
    }

    public void delete(String id, String ownerUsername) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (!task.getUser().equals(ownerUsername)) {
            throw new ForbiddenActionException("This task cannot be deleted");
        }

        repository.deleteById(id);

        log.info("Deleted task with ID {}", id);
    }
}
