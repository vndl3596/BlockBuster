package com.example.blockbuster.controller;

import com.example.blockbuster.apiCall.DataCall;
import com.example.blockbuster.dto.*;
import com.example.blockbuster.dto.address.TownDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

@RestController
@RequestMapping("/")
public class WatchMovieController {

    AccountDTO acc = new AccountDTO();

    @RequestMapping("watch-movie/id={id}")
    public ModelAndView watchMovie(@PathVariable int id,
                                   Model model,
                                   HttpSession session,
                                   HttpServletResponse response) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        RestTemplate restTemplate = new RestTemplate();
        ArrayList<MovieEvaluateDTO> evaluateList = new ArrayList<>();

        String uriAllEvaluateOnMovie = "http://localhost:8080/api/movieDetail/loadEvaluateInMovie/" + id;
        ResponseEntity<MovieEvaluateDTO[]> responseAllEvaluateOnMovie = restTemplate.getForEntity(uriAllEvaluateOnMovie, MovieEvaluateDTO[].class);
        Collections.addAll(evaluateList, responseAllEvaluateOnMovie.getBody());

        float rate = 0;
        for (MovieEvaluateDTO eva: evaluateList) {
            rate += (float) eva.getEvaluateRate();
        }
        rate = rate/(float) evaluateList.size();
        float tmpRate = rate;
        ArrayList<Float> starList = new ArrayList<>();
        if(tmpRate>0){
            for (int i = 0; i < 5; i++){
                if(tmpRate>=1){
                    starList.add(1F);
                }else if((tmpRate>0)&&(tmpRate<1)){
                    starList.add(0.5F);
                }else starList.add(0F);
                tmpRate--;
            }}

        ArrayList<MovieDTO> listAllMovie;
        listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
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
        LoginResponse loginResponse;
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if(loginResponse != null){
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");

            model.addAttribute("loginAcc", loginAcc);
            String urlSaveUserHistory = "http://localhost:8080/api/history/add-history/" + loginAcc.getId() + "/" + id;
            restTemplate.getForEntity(urlSaveUserHistory, UserHistoryDTO.class);

//            HttpEntity<MovieDTO> requestBody = new HttpEntity<>(mv);
//            String urlIncreaseView = "http://localhost:8080/api/movieDetail/editMovie";
//            restTemplate.exchange(urlIncreaseView, HttpMethod.PUT, requestBody, MovieDTO.class);
        }
        else {
            session.setAttribute("oldUrl","/watch-movie/id=" + id);
            response.sendRedirect("/login");
            return null;
        }
        model.addAttribute("df", df);
        model.addAttribute("rate", rate);
        model.addAttribute("starList", starList);
        model.addAttribute("evaluateList", evaluateList);
        model.addAttribute("loginResponse",loginResponse);
        model.addAttribute("movie", mv);
        model.addAttribute("acc", acc);

        return new ModelAndView("watchmovie");
    }
}
