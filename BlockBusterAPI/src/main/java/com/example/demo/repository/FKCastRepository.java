package com.example.demo.repository;

import com.example.demo.model.FKCast;
import com.example.demo.model.Key.FkCastKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FKCastRepository extends JpaRepository<FKCast, FkCastKey> {
}
