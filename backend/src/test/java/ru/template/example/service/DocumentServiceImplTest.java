package ru.template.example.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.template.example.controller.dto.DocumentDto;
import ru.template.example.controller.dto.Status;
import ru.template.example.documents.service.DocumentServiceImpl;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentServiceImplTest {
    @Autowired
    private DocumentServiceImpl documentService;
    @Test
    public void saveGetTest() {
        DocumentDto documentDto = new DocumentDto(
                1L,
                "type",
                "organization",
                "description",
                "patient",
                Date.from(Instant.now()),
                Status.of("1", "NEW")
        );
        documentService.save(documentDto);

        assertEquals(1, documentDto.getId());
        assertEquals("type",documentDto.getType());
        assertEquals("organization",documentDto.getOrganization());
        assertEquals("description",documentDto.getDescription());
        assertEquals("patient",documentDto.getPatient());
    }

    @Test
    void update() {
        DocumentDto documentDto = new DocumentDto(
                1L,
                "type",
                "organization",
                "description",
                "patient",
                Date.from(Instant.now()),
                Status.of("1", "NEW")) ;
        documentService.save(documentDto);

        documentDto.setDescription("test");
        documentService.update(documentDto);

        assertEquals("test", documentDto.getDescription());
    }

    @Test
    void delete() {
        DocumentDto documentDto = new DocumentDto(
                3L,
                "type",
                "organization",
                "description",
                "patient",
                Date.from(Instant.now()),
                Status.of("1", "NEW")) ;
        documentService.save(documentDto);

        documentService.delete(3L);
        assertEquals(2, documentService.findAll().size());
    }

    @Test
    void deleteAll() {
        DocumentDto documentDto = new DocumentDto(
                4L,
                "type",
                "organization",
                "description",
                "patient",
                Date.from(Instant.now()),
               Status.of("1", "NEW"));
        DocumentDto documentDto1 = new DocumentDto(
                5L,
                "type",
                "organization",
                "description",
                "patient",
                Date.from(Instant.now()),
                Status.of("1", "NEW"));
        documentService.save(documentDto);
        documentService.save(documentDto1);
        documentService.deleteAll(Set.of(4L, 5L));
        assertEquals(2, documentService.findAll().size());
    }

    @Test
    void findAll() {
       Map<Long, DocumentDto> allDocumentMap = documentService.findAll()
                .stream()
                .collect(Collectors.toMap(DocumentDto::getId, Function.identity()));
        assertEquals(2, allDocumentMap.size());
        assertNotNull(allDocumentMap.get(1L));
        assertNotNull(allDocumentMap.get(2L));
        assertNull(allDocumentMap.get(5L));
    }

    @Test
    void getWhenNotExistsTest() {
        Assertions.assertThrows(NoSuchElementException.class, () -> documentService.get(5L));
    }
}
