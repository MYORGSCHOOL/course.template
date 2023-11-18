package ru.template.example.documents.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.entity.*;
import ru.template.example.documents.repository.DocumentRepository;
import ru.template.example.documents.repository.MessageForKafkaRepository;
import ru.template.example.documents.repository.StatusRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Lazy
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final StatusRepository statusRepository;
    private final MessageForKafkaRepository messageForKafkaRepository;
    private final ObjectMapper objectMapper;
    private final MapperFacade mapperFacade = new DefaultMapperFactory
            .Builder()
            .build()
            .getMapperFacade();

    public DocumentDto save(DocumentDto documentDto) {
        DocumentEntity document = mapperFacade.map(documentDto, DocumentEntity.class);
        Optional<StatusEntity> status = statusRepository.findByCode("NEW");
        document.setStatus(status.get());
        document.setDate(Instant.now());
        documentRepository.save(document);
        return mapperFacade.map(document, DocumentDto.class);
    }


    public DocumentDto update(DocumentDto documentDto) {
        Optional<DocumentEntity> documentEntityOptional = documentRepository.findById(documentDto.getId());
        Optional<StatusEntity> status = statusRepository.findByCode(documentDto.getStatus().getCode());
        documentRepository.updateStatusById(status.get(), documentDto.getId());
        return documentDto;
    }

    public DocumentDto sendOnApprove(DocumentDto documentDto) {
        Optional<DocumentEntity> documentEntityOptional = documentRepository.findById(documentDto.getId());
        Optional<StatusEntity> status = statusRepository.findByCode("IN_PROCESS");
        addToTableForKafkaSender(documentDto);
        documentRepository.updateStatusById(status.get(), documentDto.getId());
        return documentDto;
    }

    public void delete(Long id) {
        documentRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(Set<Long> ids) {
        ids.forEach(this::delete);
    }

    public List<DocumentDto> findAll() {
        List<DocumentEntity> documents
                = documentRepository.findAll();
        return mapperFacade.mapAsList(documents, DocumentDto.class);
    }

    public DocumentDto get(Long id) {
        Optional<DocumentEntity> document = documentRepository.findById(id);
        return mapperFacade.map(document.get(), DocumentDto.class);
    }


    /**
     * Добавить сообщения с документом в табличку для отправки сообщений
     *
     * @param documentDto документ
     */
    private void addToTableForKafkaSender(DocumentDto documentDto) {
        MessageForKafkaEntity message = new MessageForKafkaEntity();
        try {
            String json = objectMapper.writeValueAsString(documentDto);
            message.setMessage(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        message.setId(UUID.randomUUID());
        message.setSend(false);
        message.setCreateDate(Instant.now());
        messageForKafkaRepository.save(message);
    }
}
