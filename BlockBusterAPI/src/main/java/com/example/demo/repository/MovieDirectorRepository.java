package com.example.demo.repository;

import com.example.demo.model.MovieDirector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDirectorRepository extends JpaRepository<MovieDirector, Integer> {

}
