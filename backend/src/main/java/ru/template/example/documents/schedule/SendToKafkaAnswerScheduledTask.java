package ru.template.example.documents.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.template.example.documents.entity.MessageForKafkaAnswerEntity;
import ru.template.example.documents.kafka.KafkaSender;
import ru.template.example.documents.repository.MessageForKafkaAnswerRepository;

import java.util.List;

/**
 * Периодическая отправка сообщений с ответами в кафку
 */
@Component
@AllArgsConstructor
public class SendToKafkaAnswerScheduledTask {
    private static final long TIME_RATE = 10000;
    private static final String KAFKA_TOPIC_NAME = "answer";
    private final KafkaSender kafkaSender;
    private final MessageForKafkaAnswerRepository messageForKafkaAnswerRepository;

    @Scheduled(fixedRate = TIME_RATE)
    public void sendMessagesToKafka() {
        List<MessageForKafkaAnswerEntity> messages = messageForKafkaAnswerRepository.findBySendFalse();
        messages.forEach((message) -> {
            kafkaSender.sendMessage(message.getMessage(), message.getId().toString(), KAFKA_TOPIC_NAME);
            message.setSend(true);
            messageForKafkaAnswerRepository.save(message);
        });
    }
}
