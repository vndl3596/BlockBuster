package com.example.blockbuster.apicall;

import com.example.blockbuster.dto.GenreDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;

public class GenreCall {
    private static RestTemplate restTemplate = new RestTemplate();

    public static ArrayList<GenreDTO> getAllGenre() {
        ArrayList<GenreDTO> listGenre = new ArrayList<>();
        String uriAllGenre = "http://localhost:8080/api/genre/getAll";
        ResponseEntity<GenreDTO[]> responseAllGenre = restTemplate.getForEntity(uriAllGenre, GenreDTO[].class);
        Collections.addAll(listGenre, responseAllGenre.getBody());
        return listGenre;
    }

    public static ArrayList<GenreDTO> getGenreByMovieId(int movie) {
        ArrayList<GenreDTO> listGenre = new ArrayList<>();
        String uriMVGenre = "http://localhost:8080/api/fkGenre/getAllGenre/" + movie;
        ResponseEntity<GenreDTO[]> responseMVGenre = restTemplate.getForEntity(uriMVGenre, GenreDTO[].class);
        Collections.addAll(listGenre, responseMVGenre.getBody());
        return listGenre;
    }
}
