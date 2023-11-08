package ru.template.example.documents.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Column;
import java.time.Instant;
import java.util.Date;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "documents")
public class DocumentsEntity {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "organization")
    private String organization;
    @Column(name = "description")
    private String description;
    @Column(name = "patient")
    private String patient;
    @Column(name = "date")
    private Date date;
    @Column(name = "state")
    private String state;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
