package com.danilowskic.nbd_project.service;

import com.danilowskic.nbd_project.exception.TaskNotFoundException;
import com.danilowskic.nbd_project.model.Task;
import com.danilowskic.nbd_project.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskQueryService {

    private final TaskRepository repository;

    private final MongoTemplate mongoTemplate;

    public TaskQueryService(TaskRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public Task getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> search(String owner, List<Pair<String, String>> filters) {
        Query query = new Query();

        if (owner != null && !owner.isEmpty()) {
            query.addCriteria(Criteria.where("user").is(owner));
        }

        filters.forEach(pair -> query
                .addCriteria(
                        Criteria.where(pair.getFirst())
                                .is(pair.getSecond())
                )
        );

        List<Task> tasks = mongoTemplate.find(query, Task.class);

        log.info("Found {} tasks", tasks.size());

        return tasks;
    }
}
