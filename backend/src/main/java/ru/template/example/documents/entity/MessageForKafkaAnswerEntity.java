package ru.template.example.documents.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Сущность для отправки ответов на сообщения из кафки
 */
@Getter
@Setter
@Entity
@Table(name = "message_for_kafka_answer")
public class MessageForKafkaAnswerEntity {

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