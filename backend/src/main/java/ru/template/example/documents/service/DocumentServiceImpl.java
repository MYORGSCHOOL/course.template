package ru.template.example.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.template.example.controller.dto.DocumentDto;
import ru.template.example.documents.repository.DocumentRepository;
import ru.template.example.documents.repository.StatusRepository;
import ru.template.example.documents.repository.entity.DocumentsEntity;
import ru.template.example.documents.repository.entity.StatusEntity;
import ru.template.example.exception.DocumentIllegalStatusException;
import ru.template.example.exception.DocumentNotFoundException;
import ru.template.example.exception.NotFoundStatus;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Lazy
@Slf4j
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final StatusRepository statusRepository;
    private final KafkaSender kafkaSender;

    private final MapperFacade mapperFacade = new DefaultMapperFactory
            .Builder()
            .build()
            .getMapperFacade();

    /**
     * Сохранение документа
     *
     * @param documentDto документ
     * @return документ
     */
    @Transactional
    public DocumentDto save(DocumentDto documentDto) {
        DocumentsEntity document = mapperFacade.map(documentDto, DocumentsEntity.class);
        Optional<StatusEntity> status = statusRepository.findByCode("NEW");
        document.setStatus(status.orElseThrow(() -> new NotFoundStatus("Статус не найден")));
        document.setDate(Date.from(Instant.now()));
        documentRepository.save(document);
        return mapperFacade.map(document, DocumentDto.class);
    }

    /**
     * Обновить документ и отправить в кафку
     *
     * @param documentDto документ
     * @return документ
     */
    @Transactional
    public DocumentDto update(DocumentDto documentDto) {
        long id = documentDto.getId();
        DocumentsEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Документ не найден."));
        if (document.getStatus().equals("NEW")) {
            throw new DocumentIllegalStatusException("Неверный статус документа.");
        }
        kafkaSender.sendMessage(documentDto);
        Optional<StatusEntity> status = statusRepository.findByCode("IN_PROCESS");
        documentRepository.updateStatusById(status.orElseThrow(() -> new NotFoundStatus("Статус не найден")), documentDto.getId());
        return documentDto;
    }

    /**
     * Удаление одного документа по id
     *
     * @param id идентификатор документа
     */
    @Transactional
    public void delete(Long id) {
        documentRepository.deleteById(id);
    }

    /**
     * Удаление нескольких документов
     *
     * @param ids идентификаторы документов
     */
    @Transactional
    public void deleteAll(Set<Long> ids) {
        ids.forEach(this::delete);
    }

    /**
     * Получение списка всех документов
     *
     * @return список документов
     */
    @Transactional
    public List<DocumentDto> findAll() {
        List<DocumentsEntity> document = documentRepository.findAll();
        return mapperFacade.mapAsList(document, DocumentDto.class);
    }

    /**
     * Получение документа по id
     *
     * @param id идентификатор документа
     * @return документ
     */
    @Transactional
    public DocumentDto get(Long id) {
        Optional<DocumentsEntity> document = documentRepository.findById(id);
        return mapperFacade.map(document.orElseThrow(() -> new DocumentNotFoundException("Документ не найден.")), DocumentDto.class);
    }
}
