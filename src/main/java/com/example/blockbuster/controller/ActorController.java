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
public class ActorController {
    ArrayList<GenreDTO> listGenre;
    ArrayList<MovieDTO> list = new ArrayList<>();

    int actId = 0;

    @RequestMapping("celebrity/actor/id={id}&page={page}")
    public ModelAndView actorDetail(HttpSession session,
                                    Model model,
                                    @PathVariable("id") int actor,
                                    @PathVariable("page") int page) {
        DecimalFormat df = new DecimalFormat("#.#");
        int pageNum = 10;
        list.removeAll(list);
        ArrayList<CastDTO> listAllCast;
        listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
        if (listAllCast == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
        }

        CastDTO cast = new CastDTO();
        for (CastDTO c : listAllCast) {
            if (c.getId() == actor) {
                cast = c;
                actId = c.getId();
            }
        }

        listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
        RestTemplate restTemplate = new RestTemplate();

        String uriMovieOnCast = "http://localhost:8080/api/fkCast/movie/" + actor;
        ResponseEntity<MovieDTO[]> responseMovieOnCast = restTemplate.getForEntity(uriMovieOnCast, MovieDTO[].class);
        Collections.addAll(list, responseMovieOnCast.getBody());

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

        session.setAttribute("oldUrl", "/celebrity/actor/id=" + actor + "&page=" + page);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse",loginResponse);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("cast", cast);
        return new ModelAndView("actor");
    }

    @RequestMapping("celebrity/actor/page-select/page={page}")
    public ModelAndView selectPageActor(HttpServletResponse httpServletResponse,
                                        @PathVariable("page") int page) throws IOException {
        httpServletResponse.sendRedirect("/celebrity/actor/id=" + actId + "&page=" + page);
        return null;
    }

}
