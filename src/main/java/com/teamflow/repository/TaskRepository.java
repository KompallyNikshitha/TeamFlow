package com.teamflow.repository;

import com.teamflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(String status);

    long countByStatus(String status);

    List<Task> findByEmployee_Id(Long employeeId);

    long countByEmployee_Id(Long employeeId);

    long countByEmployee_IdAndStatus(Long employeeId, String status);
}