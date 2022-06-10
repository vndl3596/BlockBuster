package com.example.demo.repository;

import com.example.demo.model.FKGenre;
import com.example.demo.model.Key.FkGenreKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FKGenreRepository extends JpaRepository<FKGenre, FkGenreKey> {
}
