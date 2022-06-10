package com.example.demo.repository;

import com.example.demo.model.FKDirector;
import com.example.demo.model.Key.FkDirectorKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FKDirectorRepository extends JpaRepository<FKDirector, FkDirectorKey> {
}
