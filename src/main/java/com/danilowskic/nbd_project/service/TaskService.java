package com.danilowskic.nbd_project.service;

import com.danilowskic.nbd_project.model.Task;
import com.danilowskic.nbd_project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Task getById(String id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> findAll() {
        return taskRepository.findAll(
                Sort.by(Sort.Direction.DESC, "priority")
        );
    }

    public void save(Task task) {
        taskRepository.save(task);
    }

    public void delete(String id) {
        taskRepository.deleteById(id);
    }

    public List<Map> getTaskCountByCategory() {
        Aggregation aggregation = newAggregation(
                group("category").count().as("totalTasks"),
                project("totalTasks").and("category").previousOperation(),
                sort(Sort.Direction.DESC, "totalTasks")
        );

        AggregationResults<Map> groupResults = mongoTemplate.aggregate(
                aggregation, Task.class, Map.class
        );

        return groupResults.getMappedResults();
    }

    public double getAveragePriority() {
        Aggregation aggregation = newAggregation(
                group().avg("priority").as("avgPriority")
        );

        AggregationResults<Map> result = mongoTemplate.aggregate(
                aggregation, Task.class, Map.class
        );

        if (result.getUniqueMappedResult() != null) {
            return (double) result.getUniqueMappedResult().get("avgPriority");
        }

        return 0.0;
    }

    public List<Task> searchTasks(String category, Integer priority, String project) {
        Query query = new Query();

        if (category != null && !category.isEmpty()) {
            query.addCriteria(Criteria.where("category").is(category));
        }
        if (priority != null) {
            query.addCriteria(Criteria.where("priority").is(priority));
        }

        if (project != null && !project.isEmpty()) {
            query.addCriteria(Criteria.where("attributes.project").is(project));
        }

        return mongoTemplate.find(query, Task.class);
    }

    public List<Map> getStatsByProject() {
        Aggregation aggregation = newAggregation(
                group("attributes.project").count().as("count"),
                project("count").and("_id").as("projectName"),
                match(Criteria.where("projectName").ne(null))
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, Task.class, Map.class);
        return results.getMappedResults();
    }
}
