package ru.template.example.documents.service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.repository.DocumentRepository;
import ru.template.example.documents.store.DocumentStore;
import org.springframework.context.annotation.Lazy;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Lazy
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final MapperFacade mapperFacade = new DefaultMapperFactory
            .Builder()
            .build()
            .getMapperFacade();
    public DocumentDto save(DocumentDto documentDto) {
        if (documentDto.getId() == null) {
            documentDto.setId(RandomUtils.nextLong(0L, 999L));
        }
        documentDto.setDate(new Date());
        if (documentDto.getStatus() == null) {
            documentDto.setStatus(Status.of("NEW", "Новый"));
        }
        DocumentStore.getInstance().getDocumentDtos().add(documentDto);
        return documentDto;
    }


    public DocumentDto update(DocumentDto documentDto) {
        List<DocumentDto> documentDtos = DocumentStore.getInstance().getDocumentDtos();
        Optional<DocumentDto> dto = documentDtos.stream()
                .filter(d -> d.getId().equals(documentDto.getId())).findFirst();
        if (dto.isPresent()) {
            delete(documentDto.getId());
            save(documentDto);
        }
        return documentDto;
    }

    public void delete(Long id) {
        List<DocumentDto> documentDtos = DocumentStore.getInstance().getDocumentDtos();
        List<DocumentDto> newList = documentDtos.stream()
                .filter(d -> !d.getId().equals(id)).collect(Collectors.toList());
        documentDtos.clear();
        documentDtos.addAll(newList);
    }

    public void deleteAll(Set<Long> ids) {
        List<DocumentDto> documentDtos = DocumentStore.getInstance().getDocumentDtos();
        List<DocumentDto> newList = documentDtos.stream()
                .filter(d -> !ids.contains(d.getId())).collect(Collectors.toList());
        documentDtos.clear();
        documentDtos.addAll(newList);
    }

    public List<DocumentDto> findAll() {
        return DocumentStore.getInstance().getDocumentDtos();
    }

    public DocumentDto get(Long id) {
        List<DocumentDto> documentDtos = DocumentStore.getInstance().getDocumentDtos();
        return documentDtos.stream()
                .filter(d -> d.getId().equals(id)).findFirst().get();
    }
}
