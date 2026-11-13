
package com.teamflow.controller;

import com.teamflow.entity.Task;
import com.teamflow.entity.Project;
import com.teamflow.entity.Employee;

import com.teamflow.repository.TaskRepository;
import com.teamflow.repository.ProjectRepository;
import com.teamflow.repository.EmployeeRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    public TaskController(TaskRepository taskRepository,
                          ProjectRepository projectRepository,
                          EmployeeRepository employeeRepository) {

        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    // View all tasks and filter by status
    @GetMapping
    public String listTasks(
            @RequestParam(required = false) String status,
            Model model) {

        if (status == null || status.isBlank()) {
            model.addAttribute("tasks", taskRepository.findAll());
        } else {
            model.addAttribute("tasks",
                    taskRepository.findByStatus(status));
        }

        model.addAttribute("newTask", new Task());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());

        return "tasks";
    }

    // Add task
    @PostMapping("/add")
    public String addTask(
            @ModelAttribute Task task,
            @RequestParam Long projectId,
            @RequestParam(required = false) Long employeeId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid project ID: " + projectId));

        task.setProject(project);

        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Invalid employee ID: " + employeeId));

            task.setEmployee(employee);
        }

        taskRepository.save(task);

        return "redirect:/tasks";
    }

    // Open edit page
    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid task ID: " + id));

        model.addAttribute("task", task);
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());

        return "edit-task";
    }

    // Update task
    @PostMapping("/update/{id}")
    public String updateTask(
            @PathVariable Long id,
            @ModelAttribute Task task,
            @RequestParam Long projectId,
            @RequestParam(required = false) Long employeeId) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid task ID: " + id));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid project ID: " + projectId));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setDeadline(task.getDeadline());
        existingTask.setProject(project);

        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Invalid employee ID: " + employeeId));

            existingTask.setEmployee(employee);
        }

        taskRepository.save(existingTask);

        return "redirect:/tasks";
    }

    // Delete task
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {

        taskRepository.deleteById(id);

        return "redirect:/tasks";
    }
}
