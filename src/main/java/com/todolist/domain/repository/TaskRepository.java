package com.todolist.domain.repository;

import com.todolist.domain.entity.Task;
import com.todolist.domain.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    List<Task> findByTitleContainingIgnoreCase(String title);
    Page<Task> findAll(Pageable pageable);
}
