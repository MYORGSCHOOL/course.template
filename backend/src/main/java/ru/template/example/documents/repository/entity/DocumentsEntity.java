package ru.template.example.documents.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import java.util.Date;

/**
 * Сущность документ
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "documents")
public class DocumentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * Номер
     */
    @Column(name = "id")
    private Long id;
    /**
     * Вид документа
     */
    @Column(name = "type")
    private String type;
    /**
     * Медицинская организация
     */
    @Column(name = "organization")
    private String organization;
    /**
     * Описание документа
     */
    @Column(name = "description")
    private String description;
    /**
     * ФИО пациента
     */
    @Column(name = "patient")
    private String patient;
    /**
     * Дата создания документа
     */
    @Column(name = "date")
    private Date date;
    /**
     * Статус документа
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private StatusEntity status;
}
