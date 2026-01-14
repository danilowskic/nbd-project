package com.danilowskic.nbd_project.service;

import com.danilowskic.nbd_project.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
@Service
public class TaskStatisticsService {

    private final MongoTemplate mongoTemplate;

    public TaskStatisticsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Map> getStatsByProject(String owner) {
        log.info("Getting stats for owner {}", owner);

        List<AggregationOperation> operations = new ArrayList<>();

        if (owner != null && !owner.isEmpty()) {
            operations.add(match(Criteria.where("user").is(owner)));
        }

        operations.add(group("attributes.project").count().as("count"));
        operations.add(project("count").and("_id").as("projectName"));
        operations.add(match(Criteria.where("projectName").ne(null)));

        Aggregation aggregation = newAggregation(operations);

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, Task.class, Map.class);
        return results.getMappedResults();
    }

    public double getAveragePriority(String owner) {
        log.info("Getting average priority for owner {}", owner);

        List<AggregationOperation> operations = new ArrayList<>();

        if (owner != null && !owner.isEmpty()) {
            operations.add(match(Criteria.where("user").is(owner)));
        }

        operations.add(group().avg("priority").as("avgVal"));

        Aggregation aggregation = newAggregation(operations);

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, Task.class, Map.class);

        return results.getUniqueMappedResult() != null
                ? (double) results.getUniqueMappedResult().get("avgVal")
                : 0.0;
    }
}
