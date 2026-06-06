package com.todolist.application.usecase;

import com.todolist.application.dto.CreateTaskRequestDTO;
import com.todolist.application.dto.TaskResponseDTO;
import com.todolist.application.dto.UpdateTaskRequestDTO;
import com.todolist.domain.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskUseCase {
    TaskResponseDTO createTask(CreateTaskRequestDTO request);
    TaskResponseDTO getTaskById(Long id);
    Page<TaskResponseDTO> getAllTasks(Pageable pageable);
    List<TaskResponseDTO> getTasksByStatus(TaskStatus status);
    Page<TaskResponseDTO> getTasksByStatusPaginated(TaskStatus status, Pageable pageable);
    List<TaskResponseDTO> searchTasksByTitle(String title);
    TaskResponseDTO updateTask(Long id, UpdateTaskRequestDTO request);
    void deleteTask(Long id);
}
