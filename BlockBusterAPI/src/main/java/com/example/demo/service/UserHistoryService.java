package com.example.demo.service;

import com.example.demo.dto.UserHistoryDTO;

import java.text.ParseException;
import java.util.List;

public interface UserHistoryService {
    String createHistory(UserHistoryDTO userHistoryDTO);
    void deleteUserHistoryFromAccount(int userId);

    UserHistoryDTO addHistory(Integer idAcc, Integer idMovie, String movieTime) throws ParseException;

    List<UserHistoryDTO> getAllByAccount(int id);

    UserHistoryDTO getHistoryByAccountAndMovie(Integer idAcc, Integer idMovie);
}
