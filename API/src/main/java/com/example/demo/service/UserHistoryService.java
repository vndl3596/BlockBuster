package com.example.demo.service;

import com.example.demo.DTO.UserHistoryDTO;

import java.util.List;

public interface UserHistoryService {
    String createHistory(UserHistoryDTO userHistoryDTO);
    void deleteUserHistoryFromAccount(int userId);

    UserHistoryDTO addHistory(Integer idAcc, Integer idMovie);

    List<UserHistoryDTO> getAllByAccount(int id);
}
