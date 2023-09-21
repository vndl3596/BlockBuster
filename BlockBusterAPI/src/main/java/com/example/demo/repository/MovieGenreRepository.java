package com.example.demo.repository;

import com.example.demo.model.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Integer> {
}
