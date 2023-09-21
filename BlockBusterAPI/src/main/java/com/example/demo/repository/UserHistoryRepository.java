package com.example.demo.repository;

import com.example.demo.model.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Integer> {
}
