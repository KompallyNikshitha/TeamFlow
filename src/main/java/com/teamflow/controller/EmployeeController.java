
package com.teamflow.controller;

import com.teamflow.entity.Employee;
import com.teamflow.repository.EmployeeRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("employee", new Employee());
        return "employees";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));
        model.addAttribute("employee", employee);
        return "edit-employee";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute Employee employee) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));

        existing.setName(employee.getName());
        existing.setEmail(employee.getEmail());
        existing.setRole(employee.getRole());

        employeeRepository.save(existing);
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/employees";
    }
}
