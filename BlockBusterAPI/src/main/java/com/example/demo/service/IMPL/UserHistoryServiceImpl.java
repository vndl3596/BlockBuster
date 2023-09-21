package com.example.demo.service.IMPL;

import com.example.demo.dto.UserHistoryDTO;
import com.example.demo.map.UserHistoryMap;
import com.example.demo.model.UserHistory;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.MovieDetailRepository;
import com.example.demo.repository.UserHistoryRepository;
import com.example.demo.service.UserHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserHistoryServiceImpl implements UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;
    private final UserHistoryMap userHistoryMap;
    private final AccountRepository accountRepository;
    private final MovieDetailRepository movieDetailRepository;

    @Override
    public String createHistory(UserHistoryDTO userHistoryDTO) {
        userHistoryRepository.save(userHistoryMap.DTOToUserHistory(userHistoryDTO));
        return "add user's history successfully";
    }

    @Override
    public void deleteUserHistoryFromAccount(int userId) {
        List<UserHistory> userHistories = userHistoryRepository.findAll();
        userHistories.forEach(userHistory -> {
            if (userHistory.getUser().getId() == userId) {
                userHistoryRepository.delete(userHistory);
            }
        });
    }

    @Override
    public UserHistoryDTO addHistory(Integer idAcc, Integer idMovie, String movieTime) throws ParseException {
        List<UserHistory> userHistories = userHistoryRepository.findAll();
        UserHistory userHistory = null;
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        long time = format.parse(movieTime).getTime();
        for (UserHistory userHistoryCheck : userHistories) {
            if (Objects.equals(userHistoryCheck.getUser().getId(), idAcc) && Objects.equals(userHistoryCheck.getMovie().getId(), idMovie)) {
                userHistory = userHistoryCheck;
                userHistory.setHistoryDate(date);
                userHistory.setMovieTime(new Time(time));
                userHistoryRepository.save(userHistory);
            }
        }
        if (userHistory == null) {
            userHistory = new UserHistory();
            userHistory.setUser(accountRepository.getById(idAcc));
            userHistory.setMovie(movieDetailRepository.getById(idMovie));
            userHistory.setHistoryDate(date);
            userHistory.setMovieTime(new Time(time));
            userHistoryRepository.save(userHistory);
        }
        assert userHistory != null;
        return userHistoryMap.userHistoryToDTO(userHistory);
    }

    @Override
    public List<UserHistoryDTO> getAllByAccount(int id) {
        List<UserHistoryDTO> userHistoryDTOS = new ArrayList<>();
        for (UserHistory userHistory : userHistoryRepository.findAll()) {
            if (userHistory.getUser().getId() == id) {
                userHistoryDTOS.add(userHistoryMap.userHistoryToDTO(userHistory));
            }
        }
        return userHistoryDTOS;
    }

    @Override
    public UserHistoryDTO getHistoryByAccountAndMovie(Integer idAcc, Integer idMovie){
        List<UserHistory> userHistories = userHistoryRepository.findAll();
        for (UserHistory userHistoryCheck : userHistories) {
            if (Objects.equals(userHistoryCheck.getUser().getId(), idAcc) && Objects.equals(userHistoryCheck.getMovie().getId(), idMovie)) {
                return userHistoryMap.userHistoryToDTO(userHistoryCheck);
            }
        }
        return null;
    }
}
