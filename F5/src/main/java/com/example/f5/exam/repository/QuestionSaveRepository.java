package com.example.f5.exam.repository;

import com.example.f5.exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionSaveRepository extends JpaRepository<Question, Long> {


}
