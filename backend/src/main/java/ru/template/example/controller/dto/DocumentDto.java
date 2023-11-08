package ru.template.example.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    /**
     * Номер
     */
    private Long id;
    /**
     * Вид документа
     */
    @NotNull
    private String type;
    /**
     * Организация
     */
    @NotNull
    private String organization;
    /**
     * Описание
     */
    @NotNull
    private String description;
    /**
     * Пациент
     */
    @NotNull
    private String patient;
    /**
     * Дата документа
     */
    private Date date;
    /**
     * Статус
     */
    @Valid
    private Status status;

}
