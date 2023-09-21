package com.example.demo.repository;

import com.example.demo.model.Key.MovieEvaluateKey;
import com.example.demo.model.MovieEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieEvaluateRepository extends JpaRepository<MovieEvaluate, MovieEvaluateKey> {
}
