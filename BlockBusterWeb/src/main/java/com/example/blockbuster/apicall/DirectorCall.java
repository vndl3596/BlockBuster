package com.example.blockbuster.apicall;

import com.example.blockbuster.dto.DirectorDTO;
import com.example.blockbuster.dto.ImageDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;

public class DirectorCall {
    private static RestTemplate restTemplate = new RestTemplate();

    public static DirectorDTO getDirectorById(int director) {
        String uriDirector = "http://localhost:8080/api/director/" + director;
        ResponseEntity<DirectorDTO> responseDirector = restTemplate.getForEntity(uriDirector, DirectorDTO.class);
        DirectorDTO dir = responseDirector.getBody();
        if (dir != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", dir.getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            dir.setAvatar(response.getBody().getUrl());
        }
        return dir;
    }

    public static ArrayList<DirectorDTO> getAllDirectorOnMovie(int movie) {
        ArrayList<DirectorDTO> directorList = new ArrayList<>();
        String uriAllDirectorOnMovie = "http://localhost:8080/api/fkDirector/director/" + movie;
        ResponseEntity<DirectorDTO[]> responseAllDirectorOnMovie = restTemplate.getForEntity(uriAllDirectorOnMovie, DirectorDTO[].class);
        Collections.addAll(directorList, responseAllDirectorOnMovie.getBody());

        for (DirectorDTO d : directorList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", d.getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            d.setAvatar(response.getBody().getUrl());
        }
        return directorList;
    }
}
