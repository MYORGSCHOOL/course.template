package ru.template.example.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaResultDto {
    /**
     * Номер
     */
    @NotNull
    private Long id;
    /**
     * Статус
     */
    @NotBlank
    private String status;
}
