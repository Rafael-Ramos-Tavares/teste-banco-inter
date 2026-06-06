package com.todolist.application.usecase.impl;

import com.todolist.application.dto.CreateTaskRequestDTO;
import com.todolist.application.dto.TaskResponseDTO;
import com.todolist.application.dto.UpdateTaskRequestDTO;
import com.todolist.application.mapper.TaskMapper;
import com.todolist.application.usecase.TaskUseCase;
import com.todolist.domain.entity.Task;
import com.todolist.domain.entity.TaskStatus;
import com.todolist.domain.repository.TaskRepository;
import com.todolist.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskUseCaseImpl implements TaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponseDTO createTask(CreateTaskRequestDTO request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
                .build();

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));
        return taskMapper.toDTO(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        return tasks.map(taskMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByStatus(TaskStatus status) {
        List<Task> tasks = taskRepository.findByStatus(status);
        return taskMapper.toDTOList(tasks);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getTasksByStatusPaginated(TaskStatus status, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByStatus(status, pageable);
        return tasks.map(taskMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> searchTasksByTitle(String title) {
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        return taskMapper.toDTOList(tasks);
    }

    @Override
    public TaskResponseDTO updateTask(Long id, UpdateTaskRequestDTO request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));
        taskRepository.delete(task);
    }
}
