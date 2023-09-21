package com.example.demo.repository;

import com.example.demo.model.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCastRepository extends JpaRepository<MovieCast, Integer> {
}
