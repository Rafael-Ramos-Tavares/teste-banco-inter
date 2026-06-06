package com.todolist.application.mapper;

import com.todolist.application.dto.TaskResponseDTO;
import com.todolist.domain.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public TaskResponseDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    public List<TaskResponseDTO> toDTOList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Task toEntity(Task task) {
        return task;
    }
}
