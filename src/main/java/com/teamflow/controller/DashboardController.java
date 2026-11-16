
package com.teamflow.controller;

import com.teamflow.entity.Employee;
import com.teamflow.repository.EmployeeRepository;
import com.teamflow.repository.TaskRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class DashboardController {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;

    public DashboardController(
            EmployeeRepository employeeRepository,
            TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/manager/dashboard")
    public String managerDashboard() {
        return "manager-dashboard";
    }

    @GetMapping("/employee/dashboard")
    public String employeeDashboard(
            Authentication authentication,
            Model model) {

        String username = authentication.getName();

        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee profile not found"));

        Long employeeId = employee.getId();

        model.addAttribute("employee", employee);
        model.addAttribute("tasks",
                taskRepository.findByEmployee_Id(employeeId));

        model.addAttribute("totalTasks",
                taskRepository.countByEmployee_Id(employeeId));

        model.addAttribute("pendingTasks",
                taskRepository.countByEmployee_IdAndStatus(
                        employeeId, "To Do"));

        model.addAttribute("inProgressTasks",
                taskRepository.countByEmployee_IdAndStatus(
                        employeeId, "In Progress"));

        model.addAttribute("reviewTasks",
                taskRepository.countByEmployee_IdAndStatus(
                        employeeId, "In Review"));

        model.addAttribute("completedTasks",
                taskRepository.countByEmployee_IdAndStatus(
                        employeeId, "Completed"));

        return "employee-dashboard";
    }
}
