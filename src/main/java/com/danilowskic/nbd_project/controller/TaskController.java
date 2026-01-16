package com.danilowskic.nbd_project.controller;

import com.danilowskic.nbd_project.model.Task;
import com.danilowskic.nbd_project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        String username = principal.getName();

        model.addAttribute("tasks", taskService.searchTasks(username, Collections.emptyList()));

        model.addAttribute("statsProject", taskService.getProjectStats(username));
        model.addAttribute("avgPriority", taskService.getAveragePriority(username));
        model.addAttribute("username", username);

        return "index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new Task());
        return "form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "form";
    }

    @PostMapping("/save")
    public String saveTask(
            @ModelAttribute Task task,
            @RequestParam(required = false) String formProject,
            @RequestParam(required = false) String formDeadline,
            Principal principal
    ) {
        String username = principal.getName();

        if (formProject != null && !formProject.isEmpty()) {
            task.getAttributes().put("project", formProject);
        }
        if (formDeadline != null && !formDeadline.isEmpty()) {
            task.getAttributes().put("deadline", formDeadline);
        }

        taskService.saveTask(task, username);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable String id, Principal principal) {
        String username = principal.getName();

        taskService.deleteTask(id, username);
        return "redirect:/";
    }
}
