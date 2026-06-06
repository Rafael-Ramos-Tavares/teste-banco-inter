package com.todolist.application.dto;

import com.todolist.domain.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 255, message = "Título deve ter entre 3 e 255 caracteres")
    private String title;

    @Size(max = 5000, message = "Descrição não pode exceder 5000 caracteres")
    private String description;

    private TaskStatus status;
}
