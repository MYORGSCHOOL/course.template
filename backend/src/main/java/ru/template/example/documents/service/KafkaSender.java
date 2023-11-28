package ru.template.example.documents.service;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.template.example.controller.dto.DocumentDto;

import java.util.concurrent.ExecutionException;

@Component
@AllArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<Long, DocumentDto> kafkaTemplate;

    /**
     * Метод для отправки сообщения в кафку
     *
     * @param message сообщение
     */
    public void sendMessage(DocumentDto message) {

        ListenableFuture<SendResult<Long, DocumentDto>> future =
                kafkaTemplate.send("documentsIn", message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("failure");
            }

            @Override
            public void onSuccess(SendResult<Long, DocumentDto> result) {
                System.out.println("success");
            }
        });
        try {

            SendResult<Long, DocumentDto> sendResult = future.get();
            System.out.println(sendResult.getProducerRecord());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
