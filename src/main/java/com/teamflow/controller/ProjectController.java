
package com.teamflow.controller;

import com.teamflow.entity.Project;
import com.teamflow.repository.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects",
                projectRepository.findAll());
        model.addAttribute("newProject", new Project());
        return "projects";
    }

    @PostMapping("/add")
    public String addProject(@ModelAttribute Project project) {
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
        return "redirect:/projects";
    }
    @GetMapping("/edit/{id}")
public String editProject(@PathVariable Long id, Model model) {
    Project project = projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    model.addAttribute("project", project);
    return "edit-project";
}

@PostMapping("/update")
public String updateProject(@ModelAttribute Project project) {
    projectRepository.save(project);
    return "redirect:/projects";
}
}
