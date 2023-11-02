package com.example.f5.exam.repository;

import com.example.f5.exam.dto.ExamArchiveListDTO;
import com.example.f5.exam.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchiveSaveRepository extends JpaRepository<Archive, Long> {

    //    @Query(value = "select a.idx, a.total, a.created_date, a.flag, a.grade, a.name, a.question, a.user_id from archive a where a.user_id = :userId", nativeQuery = true)
    List<ExamArchiveListDTO> findByUserId(@Param(value = "userId") String userId);

}
