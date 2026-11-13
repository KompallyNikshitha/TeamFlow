
package com.teamflow;

import com.teamflow.repository.ProjectRepository;
import com.teamflow.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public HomeController(ProjectRepository projectRepository,
                          TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("totalProjects", projectRepository.count());

        model.addAttribute("activeTasks",
        taskRepository.countByStatus("In Progress"));

       model.addAttribute("completedTasks",
        taskRepository.countByStatus("Completed"));

        return "index";
    }
}
