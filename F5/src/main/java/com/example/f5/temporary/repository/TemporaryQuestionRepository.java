package com.example.f5.temporary.repository;

import com.example.f5.temporary.entity.TemporaryQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryQuestionRepository extends JpaRepository<TemporaryQuestion, Integer> {

}
