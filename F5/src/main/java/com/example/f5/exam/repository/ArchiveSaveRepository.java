package com.example.f5.exam.repository;

import com.example.f5.exam.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveSaveRepository extends JpaRepository<Archive, Long> {

}
