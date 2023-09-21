package com.example.blockbuster.apicall;

import com.example.blockbuster.dto.UserHistoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MovieHistoryOfUserCall {
    private static RestTemplate restTemplate = new RestTemplate();

    public static ArrayList<UserHistoryDTO> getAllMovieHistoryOnUser(int user) {
        ArrayList<UserHistoryDTO> history = new ArrayList<>();
        String uriAllMovieOnUser = "http://localhost:8080/api/history/get-all-by-account/" + user;
        ResponseEntity<UserHistoryDTO[]> responseAllMovieOnUser = restTemplate.getForEntity(uriAllMovieOnUser, UserHistoryDTO[].class);
        Collections.addAll(history, responseAllMovieOnUser.getBody());
        Collections.sort(history, new Comparator<UserHistoryDTO>() {
            @Override
            public int compare(UserHistoryDTO o1, UserHistoryDTO o2) {
                int compare = -o1.getHistoryDate().compareTo(o2.getHistoryDate());
                if (compare == 0) {
                    return 1;
                } else return compare;
            }
        });
        return history;
    }
}
