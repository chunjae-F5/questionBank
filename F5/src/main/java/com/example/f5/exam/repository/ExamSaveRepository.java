package com.example.f5.exam.repository;

import com.example.f5.exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamSaveRepository extends JpaRepository<Exam, Long> {


}
