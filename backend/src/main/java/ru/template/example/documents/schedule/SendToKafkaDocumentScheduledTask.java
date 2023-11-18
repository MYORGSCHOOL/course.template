package ru.template.example.documents.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.template.example.documents.entity.MessageForKafkaEntity;
import ru.template.example.documents.repository.MessageForKafkaRepository;
import ru.template.example.documents.kafka.KafkaSender;

import java.util.List;

/**
 * Периодическая отправка сообщений с документами в кафку
 */
@Component
@AllArgsConstructor
public class SendToKafkaDocumentScheduledTask {
    private static final long TIME_RATE = 10000;
    private static final String KAFKA_TOPIC_NAME = "documents";
    private final KafkaSender kafkaSender;
    private final MessageForKafkaRepository messageForKafkaRepository;

    @Scheduled(fixedRate = TIME_RATE)
    public void sendMessagesToKafka() {
        List<MessageForKafkaEntity> messages = messageForKafkaRepository.findBySendFalse();
        messages.forEach((message) -> {
            kafkaSender.sendMessage(message.getMessage(), message.getId().toString(), KAFKA_TOPIC_NAME);
            message.setSend(true);
            message.setAccepted(false);
            messageForKafkaRepository.save(message);
        });
    }
}
