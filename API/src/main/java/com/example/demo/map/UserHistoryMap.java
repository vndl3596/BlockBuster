package com.example.demo.map;

import com.example.demo.DTO.UserHistoryDTO;
import com.example.demo.model.UserHistory;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.MovieDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserHistoryMap {
    private final AccountRepository accountRepository;
    private final MovieDetailRepository movieDetailRepository;

    public UserHistoryDTO userHistoryToDTO(UserHistory userHistory) {
        return new UserHistoryDTO(userHistory.getId(), userHistory.getUser().getId(), userHistory.getMovie().getId(), userHistory.getHistoryDate());
    }

    public List<UserHistoryDTO> listUserHistoryToDTO(List<UserHistory> userHistories) {
        List<UserHistoryDTO> userHistoryDTOS = new ArrayList<>();
        userHistories.forEach(userHistory -> {
            userHistoryDTOS.add(userHistoryToDTO(userHistory));
        });
        return userHistoryDTOS;
    }

    public UserHistory DTOToUserHistory(UserHistoryDTO userHistoryDTO) {
        return new UserHistory(
                userHistoryDTO.getId(),
                accountRepository.findById(userHistoryDTO.getUser()).orElse(null),
                movieDetailRepository.findById(userHistoryDTO.getMovie()).orElse(null),
                userHistoryDTO.getHistoryDate());
    }
}
