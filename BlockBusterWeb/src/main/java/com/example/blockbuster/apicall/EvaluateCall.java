package com.example.blockbuster.apicall;

import com.example.blockbuster.dto.ImageDTO;
import com.example.blockbuster.dto.MovieEvaluateDTO;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EvaluateCall {
    private static RestTemplate restTemplate = new RestTemplate();

    public static ArrayList<MovieEvaluateDTO> getEvaluateByMovieId(int movie) {
        ArrayList<MovieEvaluateDTO> evaluateList = new ArrayList<>();
        String uriAllEvaluateOnMovie = "http://localhost:8080/api/movieDetail/loadEvaluateInMovie/" + movie;
        ResponseEntity<MovieEvaluateDTO[]> responseAllEvaluateOnMovie = restTemplate.getForEntity(uriAllEvaluateOnMovie, MovieEvaluateDTO[].class);
        Collections.addAll(evaluateList, responseAllEvaluateOnMovie.getBody());
        Collections.sort(evaluateList, new Comparator<MovieEvaluateDTO>() {
            @Override
            public int compare(MovieEvaluateDTO o1, MovieEvaluateDTO o2) {
                return -o1.getEvaluateTime().compareTo(o2.getEvaluateTime());
            }
        });

        for (MovieEvaluateDTO me : evaluateList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", me.getAccId().getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            me.getAccId().setAvatar(response.getBody().getUrl());
        }

        return evaluateList;
    }

    public static MovieEvaluateDTO evaluateMovie(MovieEvaluateDTO movieEvaluateDTO) {
        HttpEntity<MovieEvaluateDTO> requestBody = new HttpEntity<>(movieEvaluateDTO);
        String urlEvaluate = "http://localhost:8080/api/movieDetail/saveEvaluate";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MovieEvaluateDTO> responseEntity = restTemplate.exchange(urlEvaluate, HttpMethod.PUT, requestBody, MovieEvaluateDTO.class);
        return responseEntity.getBody();
    }
}
