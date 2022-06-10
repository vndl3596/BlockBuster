package com.example.blockbuster.controller;

import com.example.blockbuster.apiCall.DataCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.GenreDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MovieDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class SearchController {
    ArrayList<MovieDTO> list;
    String sr = "";

    @RequestMapping(method = RequestMethod.GET, path = "search/page={page}")
    public ModelAndView getSearch(Model model,
                                  HttpServletResponse response,
                                  @PathVariable("page") int page,
                                  HttpSession session) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        int pageNum = 20;
        DataCall dataCall = new DataCall();
        dataCall.dataCall(session);
        list = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        if (list == null) {
            dataCall.dataCall(session);
            list = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        }
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getTitle().toLowerCase(Locale.ROOT).contains(sr)){
                list.remove(i);
                i--;
            }
        }

        ArrayList<MovieDTO> showList = new ArrayList<>();

        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == list.size()) break;
            else showList.add(list.get(i));
        }
        RestTemplate restTemplate = new RestTemplate();
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

        session.setAttribute("oldUrl", "/search/page=" + page);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse",loginResponse);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        return new ModelAndView("search");
    }

    @RequestMapping(method = RequestMethod.POST, path = "search/page={page}")
    public ModelAndView search(Model model,
                               HttpServletResponse response,
                               @RequestParam(value = "search", required = true) String search,
                               @PathVariable("page") int page,
                               HttpSession session) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        sr = search;
        int pageNum = 20;
        DataCall dataCall = new DataCall();
        dataCall.dataCall(session);
        list = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        if (list == null) {
            dataCall.dataCall(session);
            list = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        }
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getTitle().toLowerCase(Locale.ROOT).contains(search)) {
                list.remove(i);
                i--;
            }
        }

        ArrayList<MovieDTO> showList = new ArrayList<>();

        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == list.size()) break;
            else showList.add(list.get(i));
        }
        RestTemplate restTemplate = new RestTemplate();
        Map<MovieDTO, Float> showMap = new TreeMap<>(new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                return o1.getReleaseDate().compareTo(o2.getReleaseDate());
            }
        });
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

        session.setAttribute("oldUrl", "/search/page=" + page);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse",loginResponse);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        return new ModelAndView("search");
    }

    @RequestMapping("search/page-select/page={page}")
    public ModelAndView selectPageSearch(HttpServletResponse httpServletResponse,
                                              @PathVariable("page") int page) throws IOException {
        httpServletResponse.sendRedirect("/search/page=" + page);
        return null;
    }
}
