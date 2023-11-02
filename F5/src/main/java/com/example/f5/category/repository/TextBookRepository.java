package com.example.f5.category.repository;

import com.example.f5.category.entity.TextBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextBookRepository extends JpaRepository<TextBook, Integer> {

}
