package ru.template.example.documents.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

/**
 * Сущность документ
 */
@Getter
@Setter
@Entity
@Table(name = "document")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String type;

    @Column(name = "organization", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String organization;

    @Column(name = "date")
    private Instant date;

    @Column(name = "patient")
    @Type(type = "org.hibernate.type.TextType")
    private String patient;


    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private StatusEntity status;

}
