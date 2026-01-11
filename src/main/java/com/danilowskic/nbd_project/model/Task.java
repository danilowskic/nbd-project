package com.danilowskic.nbd_project.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Indexed
    @NotBlank(message = "Category must not be empty")
    private String category;

    @Min(1) @Max(5)
    private Integer priority = 3;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Map<String, Object> attributes = new HashMap<>();
}
