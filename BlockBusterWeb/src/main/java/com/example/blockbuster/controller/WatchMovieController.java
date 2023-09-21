package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.*;
import com.example.blockbuster.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class WatchMovieController {

    AccountDTO acc = new AccountDTO();
    ArrayList<MovieDTO> listAllMovie;
    LoginResponse loginResponse;

    @RequestMapping("watch-movie/id={id}")
    public ModelAndView watchMovie(@PathVariable int id,
                                   Model model,
                                   HttpSession session,
                                   HttpServletResponse response) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        RestTemplate restTemplate = new RestTemplate();
        ArrayList<MovieEvaluateDTO> evaluateList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        String uriAllEvaluateOnMovie = "http://localhost:8080/api/movieDetail/loadEvaluateInMovie/" + id;
        ResponseEntity<MovieEvaluateDTO[]> responseAllEvaluateOnMovie = restTemplate.getForEntity(uriAllEvaluateOnMovie, MovieEvaluateDTO[].class);
        Collections.addAll(evaluateList, responseAllEvaluateOnMovie.getBody());

        float rate = 0;
        for (MovieEvaluateDTO eva : evaluateList) {
            rate += (float) eva.getEvaluateRate();
        }
        rate = rate / (float) evaluateList.size();
        float tmpRate = rate;
        ArrayList<Float> starList = new ArrayList<>();
        if (tmpRate > 0) {
            for (int i = 0; i < 5; i++) {
                if (tmpRate >= 1) {
                    starList.add(1F);
                } else if ((tmpRate > 0) && (tmpRate < 1)) {
                    starList.add(0.5F);
                } else starList.add(0F);
                tmpRate--;
            }
        }

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        }

        MovieDTO mv = new MovieDTO();
        for (MovieDTO movie : listAllMovie) {
            if (movie.getId() == id) {
                mv = movie;
                mv.setViewNumber(mv.getViewNumber() + 1);
                break;
            }
        }

        if (mv.getMovieStatus() == false) {
            response.sendRedirect("/comingsoon");
        }

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);

            String urlIncreaseView = "http://localhost:8080/api/movieDetail/addView/" + id;
            restTemplate.getForEntity(urlIncreaseView, Integer.class);

            String urlGetHistoryByAccountAndMovie = "http://localhost:8080/api/history/getHistoryBy/" + id + "/" + loginAcc.getId();
            ResponseEntity<UserHistoryDTO> responseGetHistoryByAccountAndMovie = restTemplate.getForEntity(urlGetHistoryByAccountAndMovie, UserHistoryDTO.class);

            if (responseGetHistoryByAccountAndMovie.getBody() == null) {
                String urlAddFirstHistory = "http://localhost:8080/api/history/add/" + loginAcc.getId() + "/" + id + "/00:00:00";
                ResponseEntity<UserHistoryDTO> responseAdd = restTemplate.getForEntity(urlAddFirstHistory, UserHistoryDTO.class);
                model.addAttribute("history", responseAdd.getBody());
            } else {
                model.addAttribute("history", responseGetHistoryByAccountAndMovie.getBody());
            }
        } else {
            session.setAttribute("oldUrl", "/watch-movie/id=" + id);
            response.sendRedirect("/login");
            return null;
        }

        ArrayList<GenreDTO> genreList = GenreCall.getGenreByMovieId(id);
        ArrayList<DirectorDTO> directorList = DirectorCall.getAllDirectorOnMovie(id);
        ArrayList<FKCastDTO> listFKCast = ForeignKeyCastMovieCall.getAllFKCastMovie();
        Map<Integer, ArrayList<CastDTO>> characterList = new HashMap<>();
        ArrayList<CastDTO> tmpList = new ArrayList<>();
        int mapKey = 1;
        for (FKCastDTO fk : listFKCast) {
            if (tmpList.size() == 3) {
                characterList.put(mapKey, tmpList);
                tmpList = new ArrayList<>();
                mapKey += 1;
            }
            if (fk.getMovieDetailId() == id) {
                tmpList.add(CastCall.getCastById(fk.getMovieCastId()));
            }
            if (listFKCast.indexOf(fk) == listFKCast.size() - 1) {
                mapKey += 1;
                characterList.put(mapKey, tmpList);
            }
        }

        model.addAttribute("df", df);
        model.addAttribute("rate", rate);
        model.addAttribute("starList", starList);
        model.addAttribute("evaluateList", evaluateList);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("movie", mv);
        model.addAttribute("format", formatter);
        model.addAttribute("directorList", directorList);
        model.addAttribute("genreList", genreList);
        model.addAttribute("characterList", characterList);
        model.addAttribute("acc", acc);

        return new ModelAndView("watchmovie");
    }
}
