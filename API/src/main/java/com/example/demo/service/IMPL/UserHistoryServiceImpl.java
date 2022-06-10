package com.example.demo.service.IMPL;

import com.example.demo.DTO.UserHistoryDTO;
import com.example.demo.map.UserHistoryMap;
import com.example.demo.model.UserHistory;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.MovieDetailRepository;
import com.example.demo.repository.UserHistoryRepository;
import com.example.demo.service.UserHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public UserHistoryDTO addHistory(Integer idAcc, Integer idMovie) {
        UserHistory userHistory = new UserHistory();
        boolean check = false;
        for (UserHistory userHistoryL : userHistoryRepository.findAll()) {
            if ((userHistoryL.getUser().getId() == idAcc) && (userHistoryL.getMovie().getId() == idMovie)) {
                System.out.println(userHistoryL);
                userHistory = userHistoryL;
                userHistory.setHistoryDate(new Date());
                check = true;
                break;
            }
        }
        if (check == false){
            userHistory.setUser(accountRepository.getById(idAcc));
            userHistory.setMovie(movieDetailRepository.getById(idMovie));
            userHistory.setHistoryDate(new Date());
        }
        userHistoryRepository.save(userHistory);
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
}
