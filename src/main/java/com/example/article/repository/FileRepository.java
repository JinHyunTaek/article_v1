package com.example.article.repository;

import com.example.article.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {
    List<File> findByArticleId(Long articleId);
}
