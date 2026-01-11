package com.danilowskic.nbd_project.repository;

import com.danilowskic.nbd_project.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByCategory(String category);

    List<Task> findByPriorityGreaterThan(int priority);
}
