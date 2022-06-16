package com.example.blockbuster.controller;

import com.example.blockbuster.apiCall.DataCall;
import com.example.blockbuster.dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class ComingListController {

    ArrayList<MovieDTO> list;
    ArrayList<GenreDTO> listGenre;

    int genreSelect = 0;

    @RequestMapping("movie-grid/coming/page={page}")
    public ModelAndView allComingMovieGrid(HttpSession session,
                                           Model model,
                                           @PathVariable("page") int page) {
        DecimalFormat df = new DecimalFormat("#.#");
        genreSelect = 0;
        int pageNum = 20;
        DataCall dataCall = new DataCall();
        dataCall.dataCall(session);
        list = (ArrayList<MovieDTO>) session.getAttribute("listMovieComing");
        listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
        if (listGenre == null) {
            dataCall.dataCall(session);
            list = (ArrayList<MovieDTO>) session.getAttribute("listMovieComing");
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
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
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        for (MovieDTO mv : showList) {
            String uriMVRate = "http://localhost:8080/api/movieDetail/getMovieRate/" + mv.getId();
            ResponseEntity<MovieRateDTO> responseMVRate = restTemplate.getForEntity(uriMVRate, MovieRateDTO.class);
            showMap.put(mv, responseMVRate.getBody().getRate());
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
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/movie-grid/coming/page=" + page);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("genreSelect", genreSelect);
        model.addAttribute("df", df);
        return new ModelAndView("cominggrid");
    }

    @RequestMapping(method = RequestMethod.POST, value = "movie-grid/coming/select-genre")
    public ModelAndView genreComingSelectGrid(HttpServletResponse httpServletResponse,
                                              @RequestParam(value = "genreS", required = true) int genre) throws IOException {
        genreSelect = genre;
        if (genre == 0) {
            httpServletResponse.sendRedirect("/movie-grid/coming/page=1");
        } else {
            httpServletResponse.sendRedirect("/movie-grid/coming/genre=" + genre + "&page=1");
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "movie-grid/coming/genre={id}&page={page}")
    public ModelAndView genreComingMovieGrid(@PathVariable("id") int idGenre,
                                             @PathVariable("page") int page,
                                             Model model, HttpSession session) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        if (listGenre == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            list = new ArrayList<>();
        } else {
            list.removeAll(list);
        }
        RestTemplate restTemplate = new RestTemplate();
        ArrayList<MovieDTO> tmpList = new ArrayList<>();

        String uriGenreMovie = "http://localhost:8080/api/fkGenre/getAllMovie/" + idGenre;
        ResponseEntity<MovieDTO[]> responseGenreMovie = restTemplate.getForEntity(uriGenreMovie, MovieDTO[].class);
        Collections.addAll(tmpList, responseGenreMovie.getBody());
        for (MovieDTO movie : tmpList) {
            if (movie.getMovieStatus() == false) {
                list.add(movie);
            }
        }

        int pageNum = 20;
        ArrayList<MovieDTO> showList = new ArrayList<>();

        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == list.size()) break;
            else showList.add(list.get(i));
        }

        for (MovieDTO mv : showList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Map<MovieDTO, Float> showMap = new TreeMap<>(new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        for (MovieDTO mv : showList) {
            String uriMVRate = "http://localhost:8080/api/movieDetail/getMovieRate/" + mv.getId();
            ResponseEntity<MovieRateDTO> responseMVRate = restTemplate.getForEntity(uriMVRate, MovieRateDTO.class);
            showMap.put(mv, responseMVRate.getBody().getRate());
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
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");

            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/movie-grid/coming/genre=" + idGenre + "&page=" + page);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("genreSelect", genreSelect);
        model.addAttribute("df", df);
        return new ModelAndView("cominggrid");
    }

    @RequestMapping("coming-grid/transfer")
    public ModelAndView transferComingGridToList(HttpServletResponse httpServletResponse) throws IOException {
        if (genreSelect == 0)
            httpServletResponse.sendRedirect("/movie-list/coming/page=1");
        else
            httpServletResponse.sendRedirect("/movie-list/coming/genre=" + genreSelect + "&page=1");
        return null;
    }

    @RequestMapping("movie-grid/coming/page-select/page={page}")
    public ModelAndView selectPageGridComing(HttpServletResponse httpServletResponse,
                                             @PathVariable("page") int page) throws IOException {
        if (genreSelect == 0)
            httpServletResponse.sendRedirect("/movie-grid/coming/page=" + page);
        else
            httpServletResponse.sendRedirect("/movie-grid/coming/genre=" + genreSelect + "&page=" + page);
        return null;
    }

    @RequestMapping("movie-list/coming/page-select/page={page}")
    public ModelAndView selectPageListComing(HttpServletResponse httpServletResponse,
                                             @PathVariable("page") int page) throws IOException {
        if (genreSelect == 0)
            httpServletResponse.sendRedirect("/movie-list/coming/page=" + page);
        else
            httpServletResponse.sendRedirect("/movie-list/coming/genre=" + genreSelect + "&page=" + page);
        return null;
    }

    @RequestMapping("coming-list/transfer")
    public ModelAndView transferComingListToGrid(HttpServletResponse httpServletResponse) throws IOException {
        if (genreSelect == 0)
            httpServletResponse.sendRedirect("/movie-grid/coming/page=1");
        else
            httpServletResponse.sendRedirect("/movie-grid/coming/genre=" + +genreSelect + "&page=1");
        return null;
    }

    @RequestMapping("movie-list/coming/page={page}")
    public ModelAndView allComingMovieShowingList(HttpSession session,
                                                  Model model,
                                                  @PathVariable("page") int page) {
        DecimalFormat df = new DecimalFormat("#.#");
        genreSelect = 0;
        int pageNum = 5;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        DataCall dataCall = new DataCall();
        dataCall.dataCall(session);
        list = (ArrayList<MovieDTO>) session.getAttribute("listMovieComing");
        listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
        if (listGenre == null) {
            dataCall.dataCall(session);
            list = (ArrayList<MovieDTO>) session.getAttribute("listMovieComing");
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
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
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        for (MovieDTO mv : showList) {
            String uriMVRate = "http://localhost:8080/api/movieDetail/getMovieRate/" + mv.getId();
            ResponseEntity<MovieRateDTO> responseMVRate = restTemplate.getForEntity(uriMVRate, MovieRateDTO.class);
            showMap.put(mv, responseMVRate.getBody().getRate());
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
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");

            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/movie-list/coming/page=" + page);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("format", format);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("genreSelect", genreSelect);
        return new ModelAndView("cominglist");
    }

    @RequestMapping(method = RequestMethod.POST, value = "movie-list/coming/select-genre")
    public ModelAndView genreComingSelectList(HttpServletResponse httpServletResponse,
                                              @RequestParam(value = "genreS", required = true) int genre) throws IOException {
        genreSelect = genre;
        if (genre == 0) {
            httpServletResponse.sendRedirect("/movie-grid/coming/page=1");
        } else {
            httpServletResponse.sendRedirect("/movie-list/coming/genre=" + genre + "&page=1");
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "movie-list/coming/genre={id}&page={page}")
    public ModelAndView genreComingMovieList(@PathVariable("id") int idGenre,
                                             @PathVariable("page") int page,
                                             Model model, HttpSession session) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        if (listGenre == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            list = new ArrayList<>();
        } else {
            list.removeAll(list);
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();
        ArrayList<MovieDTO> tmpList = new ArrayList<>();

        String uriGenreMovie = "http://localhost:8080/api/fkGenre/getAllMovie/" + idGenre;
        ResponseEntity<MovieDTO[]> responseGenreMovie = restTemplate.getForEntity(uriGenreMovie, MovieDTO[].class);
        Collections.addAll(tmpList, responseGenreMovie.getBody());
        for (MovieDTO movie : tmpList) {
            if (movie.getMovieStatus() == false) {
                list.add(movie);
            }
        }

        int pageNum = 5;
        ArrayList<MovieDTO> showList = new ArrayList<>();

        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == list.size()) break;
            else showList.add(list.get(i));
        }
        for (MovieDTO mv : showList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Map<MovieDTO, Float> showMap = new TreeMap<>(new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        for (MovieDTO mv : showList) {
            String uriMVRate = "http://localhost:8080/api/movieDetail/getMovieRate/" + mv.getId();
            ResponseEntity<MovieRateDTO> responseMVRate = restTemplate.getForEntity(uriMVRate, MovieRateDTO.class);
            showMap.put(mv, responseMVRate.getBody().getRate());
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
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");

            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/movie-list/coming/genre=" + idGenre + "&page=" + page);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("format", format);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("genreSelect", genreSelect);

        return new ModelAndView("cominglist");
    }
}
