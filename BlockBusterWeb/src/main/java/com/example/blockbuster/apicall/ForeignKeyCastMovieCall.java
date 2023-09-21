package com.example.blockbuster.apicall;

import com.example.blockbuster.dto.FKCastDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;

public class ForeignKeyCastMovieCall {
    private static RestTemplate restTemplate = new RestTemplate();

    public static ArrayList<FKCastDTO> getAllFKCastMovie() {
        ArrayList<FKCastDTO> listFKCast = new ArrayList<>();
        String uriAllFkCast = "http://localhost:8080/api/fkCast/getAllFkCast";
        ResponseEntity<FKCastDTO[]> responseAllFkCast = restTemplate.getForEntity(uriAllFkCast, FKCastDTO[].class);
        Collections.addAll(listFKCast, responseAllFkCast.getBody());
        return listFKCast;
    }
}
