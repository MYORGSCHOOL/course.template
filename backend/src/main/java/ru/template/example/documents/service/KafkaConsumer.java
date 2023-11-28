package ru.template.example.documents.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.template.example.controller.dto.KafkaResultDto;
import ru.template.example.documents.repository.DocumentRepository;
import ru.template.example.documents.repository.StatusRepository;
import ru.template.example.documents.repository.entity.StatusEntity;
import ru.template.example.exception.NotFoundStatus;

import java.util.Optional;

@Component
@AllArgsConstructor
public class KafkaConsumer {
    private final ObjectMapper objectMapper;
    private final DocumentRepository documentRepository;
    private final StatusRepository statusRepository;

    /**
     * Читает из кафки сообщения
     * Обновляет статус документа по id
     *
     * @param message сообщение из кафки
     * @throws JsonProcessingException если текст сообщения не расшифрован
     */
    @KafkaListener(topics = "documentsOut", groupId = "group_id")
    public void consume(@Payload String message) throws JsonProcessingException {
        KafkaResultDto resultDto = objectMapper.readValue(message, KafkaResultDto.class);
        Optional<StatusEntity> status = statusRepository.findByCode(resultDto.getStatus());
        documentRepository.updateStatusById(status.orElseThrow(() -> new NotFoundStatus("Статус не найден")), resultDto.getId());
    }
}
