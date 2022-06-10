package com.example.blockbuster.controller;

import com.example.blockbuster.apiCall.DataCall;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class DirectorController {
    ArrayList<GenreDTO> listGenre;
    ArrayList<MovieDTO> list = new ArrayList<>();

    int dirId = 0;

    @RequestMapping("celebrity/director/id={id}&page={page}")
    public ModelAndView directorDetail(HttpSession session,
                                       Model model,
                                       @PathVariable("id") int dir,
                                       @PathVariable("page") int page) {
        DecimalFormat df = new DecimalFormat("#.#");
        int pageNum = 10;

        list.removeAll(list);
        ArrayList<DirectorDTO> listAllDirector;
        listAllDirector = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
        if (listAllDirector == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllDirector = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
        }

        DirectorDTO cast = new DirectorDTO();
        for (DirectorDTO c : listAllDirector) {
            if (c.getId() == dir) {
                cast = c;
                dirId = c.getId();
            }
        }
        listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
        RestTemplate restTemplate = new RestTemplate();

        String uriMovieOnDirector = "http://localhost:8080/api/fkDirector/movie/" + dir;
        ResponseEntity<MovieDTO[]> responseMovieOnDirector = restTemplate.getForEntity(uriMovieOnDirector, MovieDTO[].class);
        Collections.addAll(list, responseMovieOnDirector.getBody());

        ArrayList<MovieDTO> showList = new ArrayList<>();

        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == list.size()) break;
            else showList.add(list.get(i));
        }
        Map<MovieDTO, Float> showMap = new HashMap<>();
        for (MovieDTO mv: showList){
            String uriMVRate = "http://localhost:8080/api/movieDetail/getMovieRate/" + mv.getId();
            ResponseEntity<Float> responseMVRate = restTemplate.getForEntity(uriMVRate, Float.class);
            showMap.put(mv,responseMVRate.getBody());
        }

        float totalPageFloat = (float) list.size() / pageNum;
        int totalPage = 1;
        if (totalPageFloat != (int) totalPageFloat) {
            totalPage = (int) totalPageFloat + 1;
        } else {
            totalPage = (int) totalPageFloat;
        }

        ArrayList<Integer> pageList = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            pageList.add(i);
        }

        LoginResponse loginResponse;
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if(loginResponse != null){
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");

            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/celebrity/actor/id=" + dir + "&page=" + page);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse",loginResponse);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("cast", cast);
        return new ModelAndView("director");
    }

    @RequestMapping("celebrity/director/page-select/page={page}")
    public ModelAndView selectPageDirector(HttpServletResponse httpServletResponse,
                                           @PathVariable("page") int page) throws IOException {
        httpServletResponse.sendRedirect("/celebrity/director/id=" + dirId + "&page=" + page);
        return null;
    }

}
