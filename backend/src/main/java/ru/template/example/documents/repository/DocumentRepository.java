package ru.template.example.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.template.example.documents.entity.DocumentsEntity;
public interface DocumentRepository extends JpaRepository<DocumentsEntity, Long> {
}
