package com.todolist.presentation.controller;

import com.todolist.application.dto.CreateTaskRequestDTO;
import com.todolist.application.dto.TaskResponseDTO;
import com.todolist.application.dto.UpdateTaskRequestDTO;
import com.todolist.application.usecase.TaskUseCase;
import com.todolist.domain.entity.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "API para gerenciamento de tarefas (To-Do List)")
public class TaskController {

    private final TaskUseCase taskUseCase;

    @PostMapping
    @Operation(summary = "Criar uma nova tarefa", 
               description = "Cria uma nova tarefa no sistema com título, descrição e status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskRequestDTO request) {
        TaskResponseDTO response = taskUseCase.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter tarefa por ID",
               description = "Retorna os detalhes de uma tarefa específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO response = taskUseCase.getTaskById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as tarefas",
               description = "Retorna uma lista paginada de todas as tarefas")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas",
                content = @Content(mediaType = "application/json"))
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(Pageable pageable) {
        Page<TaskResponseDTO> response = taskUseCase.getAllTasks(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar tarefas por status",
               description = "Retorna todas as tarefas com um status específico (PENDING, IN_PROGRESS, COMPLETED)")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas filtradas",
                content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(@PathVariable TaskStatus status) {
        List<TaskResponseDTO> response = taskUseCase.getTasksByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}/paginated")
    @Operation(summary = "Listar tarefas por status (paginado)",
               description = "Retorna uma lista paginada de tarefas com um status específico")
    @ApiResponse(responseCode = "200", description = "Lista paginada de tarefas",
                content = @Content(mediaType = "application/json"))
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByStatusPaginated(
            @PathVariable TaskStatus status,
            Pageable pageable) {
        Page<TaskResponseDTO> response = taskUseCase.getTasksByStatusPaginated(status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar tarefas por título",
               description = "Retorna tarefas que contêm o título informado (busca case-insensitive)")
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas",
                content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<TaskResponseDTO>> searchTasksByTitle(@RequestParam String title) {
        List<TaskResponseDTO> response = taskUseCase.searchTasksByTitle(title);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma tarefa",
               description = "Atualiza os detalhes de uma tarefa existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequestDTO request) {
        TaskResponseDTO response = taskUseCase.updateTask(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma tarefa",
               description = "Remove uma tarefa do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskUseCase.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
