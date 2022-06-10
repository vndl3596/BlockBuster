package com.example.blockbuster.controller;

import com.example.blockbuster.apiCall.DataCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.GenreDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MovieDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class HomeController {

    @RequestMapping("home")
    public ModelAndView homeShow(Model model, HttpSession session) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        ArrayList<MovieDTO> bigSliderList;
        ArrayList<MovieDTO> listMovieShowing;
        ArrayList<MovieDTO> listMovieComing;
        ArrayList<MovieDTO> listMovieNew;
        ArrayList<MovieDTO> listTrailer;
        LoginResponse loginResponse;

        DataCall dataCall = new DataCall();
        dataCall.dataCall(session);
        bigSliderList = (ArrayList<MovieDTO>) session.getAttribute("bigSliderList");
        listMovieShowing = (ArrayList<MovieDTO>) session.getAttribute("listMovieShowing");
        listMovieComing = (ArrayList<MovieDTO>) session.getAttribute("listMovieComing");
        listMovieNew = (ArrayList<MovieDTO>) session.getAttribute("listMovieNew");
        listTrailer = (ArrayList<MovieDTO>) session.getAttribute("listTrailer");

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if(loginResponse != null){
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }

        RestTemplate restTemplate = new RestTemplate();
        Map<MovieDTO, ArrayList<GenreDTO>> bigSliderMap = new HashMap<>();
        for (MovieDTO mv: bigSliderList){
            String uriMVGenre = "http://localhost:8080/api/movieDetail/getGenreByMovieId/" + mv.getId();
            ResponseEntity<GenreDTO[]> responseMVGenre = restTemplate.getForEntity(uriMVGenre, GenreDTO[].class);
            ArrayList<GenreDTO> genreList = new ArrayList<>();
            Collections.addAll(genreList, responseMVGenre.getBody());
            Collections.shuffle(genreList);
            ArrayList<GenreDTO> mvGenre = new ArrayList<>();
            for (GenreDTO genre: genreList) {
                if(mvGenre.size() < 4){
                    mvGenre.add(genre);
                }
            }
            bigSliderMap.put(mv, mvGenre);
        }

        session.setAttribute("oldUrl", "/home");
        model.addAttribute("listTrailer", listTrailer);
        model.addAttribute("loginResponse",loginResponse);
        model.addAttribute("format", format);
        model.addAttribute("listMovieShowing", listMovieShowing);
        model.addAttribute("listMovieComing", listMovieComing);
        model.addAttribute("listMovieNew", listMovieNew);
        model.addAttribute("bigSliderList", bigSliderMap);

        return new ModelAndView("home");
    }
}
