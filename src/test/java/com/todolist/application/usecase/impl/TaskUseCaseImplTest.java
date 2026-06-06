package com.todolist.application.usecase.impl;

import com.todolist.application.dto.CreateTaskRequestDTO;
import com.todolist.application.dto.TaskResponseDTO;
import com.todolist.application.mapper.TaskMapper;
import com.todolist.domain.entity.Task;
import com.todolist.domain.entity.TaskStatus;
import com.todolist.domain.repository.TaskRepository;
import com.todolist.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskUseCaseImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskUseCaseImpl taskUseCase;

    private CreateTaskRequestDTO createRequest;
    private Task task;
    private TaskResponseDTO taskResponse;

    @BeforeEach
    void setUp() {
        // Arranjar (Arrange)
        createRequest = CreateTaskRequestDTO.builder()
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.PENDING)
                .build();

        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        taskResponse = TaskResponseDTO.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateTask_Success() {
        // Arranjar
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDTO(task)).thenReturn(taskResponse);

        // Agir
        TaskResponseDTO result = taskUseCase.createTask(createRequest);

        // Afirmar
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).toDTO(any(Task.class));
    }

    @Test
    void testGetTaskById_Success() {
        // Arranjar
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(taskResponse);

        // Agir
        TaskResponseDTO result = taskUseCase.getTaskById(1L);

        // Afirmar
        assertNotNull(result);
        assertEquals(1L, result.getId());
        
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        // Arranjar
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Agir & Afirmar
        assertThrows(ResourceNotFoundException.class, () -> {
            taskUseCase.getTaskById(999L);
        });
        
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteTask_Success() {
        // Arranjar
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Agir
        taskUseCase.deleteTask(1L);

        // Afirmar
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testDeleteTask_NotFound() {
        // Arranjar
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Agir & Afirmar
        assertThrows(ResourceNotFoundException.class, () -> {
            taskUseCase.deleteTask(999L);
        });
        
        verify(taskRepository, never()).delete(any());
    }
}
