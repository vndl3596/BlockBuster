package com.example.blockbuster.apicall;

import com.example.blockbuster.dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class DataCall {
    RestTemplate restTemplate = new RestTemplate();
    ArrayList<MovieDTO> listAllMovie = new ArrayList<>();
    ArrayList<MovieDTO> bigSliderList = new ArrayList<>();
    ArrayList<MovieDTO> listMovieShowing = new ArrayList();
    ArrayList<MovieDTO> listMovieComing = new ArrayList<>();
    ArrayList<MovieDTO> listMovieNew = new ArrayList<>();
    ArrayList<GenreDTO> listGenre = new ArrayList<>();
    ArrayList<CastDTO> listCast = new ArrayList<>();
    ArrayList<DirectorDTO> listDirector = new ArrayList<>();
    ArrayList<FKCastDTO> listFKCast = new ArrayList<>();
    ArrayList<MovieDTO> listTrailer = new ArrayList<>();
    HashMap<Integer, String> imageHash = new HashMap<Integer, String>();

    public void dataCall(HttpSession session) {
        String uriAllMovie = "http://localhost:8080/api/movieDetail/getMovieDetailAll";
        ResponseEntity<MovieDTO[]> responseAllMovie = restTemplate.getForEntity(uriAllMovie, MovieDTO[].class);
        Collections.addAll(listAllMovie, responseAllMovie.getBody());
        for (MovieDTO mv : listAllMovie) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }

        String uriAllGenre = "http://localhost:8080/api/genre/getAll";
        ResponseEntity<GenreDTO[]> responseAllGenre = restTemplate.getForEntity(uriAllGenre, GenreDTO[].class);
        Collections.addAll(listGenre, responseAllGenre.getBody());

        String uriAllCast = "http://localhost:8080/api/cast/getAll";
        ResponseEntity<CastDTO[]> responseAllCast = restTemplate.getForEntity(uriAllCast, CastDTO[].class);
        Collections.addAll(listCast, responseAllCast.getBody());
        for (CastDTO castDTO : listCast) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", castDTO.getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            castDTO.setAvatar(response.getBody().getUrl());
        }

        String uriAllDirector = "http://localhost:8080/api/director/getAll";
        ResponseEntity<DirectorDTO[]> responseAllDirector = restTemplate.getForEntity(uriAllDirector, DirectorDTO[].class);
        Collections.addAll(listDirector, responseAllDirector.getBody());
        for (DirectorDTO directorDTO : listDirector) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", directorDTO.getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            directorDTO.setAvatar(response.getBody().getUrl());
        }

        String uriAllFkCast = "http://localhost:8080/api/fkCast/getAllFkCast";
        ResponseEntity<FKCastDTO[]> responseAllFkCast = restTemplate.getForEntity(uriAllFkCast, FKCastDTO[].class);
        Collections.addAll(listFKCast, responseAllFkCast.getBody());
        Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                return -o1.getReleaseDate().compareTo(o2.getReleaseDate());
            }
        });

        for (MovieDTO movie : listAllMovie) {
            if (movie.getMovieStatus() == true) {
                listMovieShowing.add(movie);
            } else listMovieComing.add(movie);
        }

        for (int i = listAllMovie.size() - 1; listMovieNew.size() < 10; i--) {
            if (i < 0) break;
            listMovieNew.add(listAllMovie.get(i));
        }

        ArrayList<MovieDTO> listTmpSlider = new ArrayList<>();
        listTmpSlider = listAllMovie;
        Collections.shuffle(listTmpSlider);
        for (MovieDTO mv : listTmpSlider) {
            if (bigSliderList.size() < 6) {
                bigSliderList.add(mv);
            }
        }

        ArrayList<MovieDTO> listTmpTrailer = new ArrayList<>();
        Collections.shuffle(listTmpTrailer);
        listTmpTrailer.addAll(listAllMovie);
        for (int i = 0; listTrailer.size() < 7; i++) {
            listTrailer.add(listTmpTrailer.get(i));
        }
        session.setAttribute("listTrailer", listTrailer);
        session.setAttribute("listAllMovie", listAllMovie);
        session.setAttribute("listGenre", listGenre);
        session.setAttribute("listMovieShowing", listMovieShowing);
        session.setAttribute("listMovieNew", listMovieNew);
        session.setAttribute("bigSliderList", bigSliderList);
        session.setAttribute("listMovieComing", listMovieComing);
        session.setAttribute("listCast", listCast);
        session.setAttribute("listDirector", listDirector);
        session.setAttribute("listFKCast", listFKCast);
        session.setAttribute("imageHash", imageHash);
    }
}
