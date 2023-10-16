package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.GenreCall;
import com.example.blockbuster.apicall.MovieCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.GenreDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MovieDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class HomeController {
    LoginResponse loginResponse;

    @RequestMapping("home")
    public ModelAndView homeShow(Model model, HttpSession session) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        ArrayList<MovieDTO> listAllMovie = MovieCall.getAllMovie();
        ArrayList<MovieDTO> listMovieShowing = MovieCall.getAllShowingMovie();
        ArrayList<MovieDTO> listMovieComing = MovieCall.getAllComingMovie();

        ArrayList<MovieDTO> listAllMovieForHome = new ArrayList<>();
        for (MovieDTO mv : listAllMovie) {
            if (listAllMovieForHome.size() < 16) listAllMovieForHome.add(mv);
        }

        ArrayList<MovieDTO> listMovieNew = new ArrayList<>();
        for (MovieDTO mv : listAllMovie) {
            if (listMovieNew.size() < 10) listMovieNew.add(mv);
        }

        Random rand = new Random();
        ArrayList<MovieDTO> bigSliderList = new ArrayList<>();
        for (int i = 0; bigSliderList.size() < 5; i++) {
            int randomIndex = rand.nextInt(listAllMovie.size());
            if (bigSliderList.indexOf(listAllMovie.get(randomIndex)) == -1)
                bigSliderList.add(listAllMovie.get(randomIndex));
        }

        ArrayList<MovieDTO> listTrailer = bigSliderList;

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }

        Map<MovieDTO, ArrayList<GenreDTO>> bigSliderMap = new HashMap<>();
        for (MovieDTO mv : bigSliderList) {
            ArrayList<GenreDTO> genreOnMovieList = GenreCall.getGenreByMovieId(mv.getId());
            Collections.shuffle(genreOnMovieList);
            ArrayList<GenreDTO> mvGenres = new ArrayList<>();
            for (GenreDTO genre : genreOnMovieList) {
                mvGenres.add(genre);
                if (mvGenres.size() == 3) break;
            }
            bigSliderMap.put(mv, mvGenres);
        }

        session.setAttribute("oldUrl", "/home");
        model.addAttribute("listTrailer", listTrailer);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("format", format);
        model.addAttribute("listMovieShowing", listMovieShowing);
        model.addAttribute("listMovieComing", listMovieComing);
        model.addAttribute("listAllMovie", listAllMovieForHome);
        model.addAttribute("listMovieNew", listMovieNew);
        model.addAttribute("bigSliderList", bigSliderMap);
        return new ModelAndView("home");
    }
}
