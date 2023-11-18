package ru.template.example.documents.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/**
 * Сущность для отправки документов сообщением в кафку
 */
@Getter
@Setter
@Entity
@Table(name = "message_for_kafka")
public class MessageForKafkaEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "message", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String message;

    @Column(name = "send")
    private Boolean send;

    @Column(name = "accepted")
    private Boolean accepted;

    @Column(name = "create_date")
    private Instant createDate;

}
