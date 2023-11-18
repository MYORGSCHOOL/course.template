package ru.template.example.documents.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.template.example.documents.controller.dto.AnswerDto;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.entity.MessageForKafkaAnswerEntity;
import ru.template.example.documents.repository.MessageForKafkaAnswerRepository;

import java.util.UUID;

/**
 * Компонент для приёма сообщений из кафки
 */
@Component
@AllArgsConstructor
public class KafkaDocumentConsumer {
    private final ObjectMapper objectMapper;
    private final MessageForKafkaAnswerRepository messageForKafkaAnswerRepository;

    /**
     * Метод слушает топик documents
     *
     * @param message текст сообщения
     * @param key     ключ сообщения
     * @throws JsonProcessingException если получен не коректный объект
     */
    @KafkaListener(topics = "documents", groupId = "group_id")
    public void consumer(@Payload String message,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) throws JsonProcessingException {
        DocumentDto documentDto = objectMapper.readValue(message, DocumentDto.class);
        checkMessageFromKafkaAnswer(UUID.fromString(key));
        AnswerDto answerDto = new AnswerDto(documentDto.getId(), "ACCEPTED");
        MessageForKafkaAnswerEntity messageForKafkaAnswer
                = prepareMessageEntity(UUID.fromString(key), answerDto);
        messageForKafkaAnswerRepository.save(messageForKafkaAnswer);
    }

    /**
     * Проверка было ли принято данное сообщение
     *
     * @param id идшник сообщения
     */
    private void checkMessageFromKafkaAnswer(UUID id) {
        var message = messageForKafkaAnswerRepository.findById(id);
        if (message.isPresent()) {
            throw new KafkaException("Problem with message");
        }
    }

    /**
     * Подготовка записи для ответного сообщения
     * @param id идентификатор сообщения
     * @param answerDto объект для передачи через сообщение
     * @return сообщения для записи в БД
     */
    private MessageForKafkaAnswerEntity prepareMessageEntity(UUID id, AnswerDto answerDto) {
        MessageForKafkaAnswerEntity messageForKafkaAnswer = new MessageForKafkaAnswerEntity();
        messageForKafkaAnswer.setId(id);
        messageForKafkaAnswer.setSend(false);
        try {
            String json = objectMapper.writeValueAsString(answerDto);
            messageForKafkaAnswer.setMessage(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return messageForKafkaAnswer;
    }
}
