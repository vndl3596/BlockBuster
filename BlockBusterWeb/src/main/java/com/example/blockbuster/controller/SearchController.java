package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.GenreCall;
import com.example.blockbuster.apicall.MovieCall;
import com.example.blockbuster.dto.*;
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
    LoginResponse loginResponse;
    int genreSelect = 0;
    String sr = "";

    @RequestMapping(method = RequestMethod.GET, path = "search/page={page}")
    public ModelAndView getSearch(Model model,
                                  HttpServletResponse response,
                                  @PathVariable("page") int page,
                                  HttpSession session) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        int pageNum = 20;
        genreSelect = 0;
        ArrayList<MovieDTO> listAllMovie = MovieCall.getAllMovie();
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();

        for (int i = 0; i < listAllMovie.size(); i++) {
            if (!listAllMovie.get(i).getTitle().toLowerCase(Locale.ROOT).contains(sr.toLowerCase(Locale.ROOT))) {
                listAllMovie.remove(i);
                i--;
            }
        }

        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listAllMovie.size()) break;
            else showList.add(listAllMovie.get(i));
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
            showMap.put(mv, MovieCall.getMovieRate(mv.getId()));
        }

        float totalPageFloat = (float) listAllMovie.size() / pageNum;
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


        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/search/page=" + page);
        model.addAttribute("sr", sr);
        model.addAttribute("genreSelect", genreSelect);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("mvNum", listAllMovie.size());
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
        genreSelect = 0;
        int pageNum = 20;
        ArrayList<MovieDTO> listAllMovie = MovieCall.getAllMovie();
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();

        for (int i = 0; i < listAllMovie.size(); i++) {
            if (!listAllMovie.get(i).getTitle().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
                listAllMovie.remove(i);
                i--;
            }
        }
        ArrayList<MovieDTO> showList = new ArrayList<>();

        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listAllMovie.size()) break;
            else showList.add(listAllMovie.get(i));
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

        float totalPageFloat = (float) listAllMovie.size() / pageNum;
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

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/search/page=" + page);
        model.addAttribute("sr", sr);
        model.addAttribute("genreSelect", genreSelect);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", listAllMovie.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        return new ModelAndView("search");
    }

    @RequestMapping(method = RequestMethod.GET, path = "search/genre={id}&page={page}")
    public ModelAndView searchFilterByGenre(Model model,
                                            @PathVariable("page") int page,
                                            @PathVariable("id") int idGenre,
                                            HttpSession session) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        genreSelect = idGenre;
        int pageNum = 20;
        ArrayList<MovieDTO> listAllMovieOnGenre = MovieCall.getAllMovieOnGenre(idGenre);
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();

        GenreDTO gen = new GenreDTO();
        for (GenreDTO g : listGenre) {
            if (g.getId() == idGenre) {
                gen = g;
            }
        }

        for (int i = 0; i < listAllMovieOnGenre.size(); i++) {
            if (!listAllMovieOnGenre.get(i).getTitle().toLowerCase(Locale.ROOT).contains(sr.toLowerCase(Locale.ROOT))) {
                listAllMovieOnGenre.remove(i);
                i--;
            }
        }

        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listAllMovieOnGenre.size()) break;
            else showList.add(listAllMovieOnGenre.get(i));
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
            showMap.put(mv, MovieCall.getMovieRate(mv.getId()));
        }

        float totalPageFloat = (float) listAllMovieOnGenre.size() / pageNum;
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

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/search/page=" + page);
        model.addAttribute("gen", gen);
        model.addAttribute("sr", sr);
        model.addAttribute("genreSelect", genreSelect);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", listAllMovieOnGenre.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        return new ModelAndView("search");
    }

    @RequestMapping("search/page-select/page={page}")
    public ModelAndView selectPageSearch(HttpServletResponse httpServletResponse,
                                         @PathVariable("page") int page) throws IOException {
        if (genreSelect == 0)
            httpServletResponse.sendRedirect("/search/page=" + page);
        else
            httpServletResponse.sendRedirect("/search/genre=" + genreSelect + "&page=" + page);
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "search/select-genre")
    public ModelAndView genreSelectGrid(HttpServletResponse httpServletResponse,
                                        @RequestParam(value = "genreS", required = true) int genre) throws IOException {
        genreSelect = genre;
        if (genre == 0) {
            httpServletResponse.sendRedirect("/search/page=1");
        } else {
            httpServletResponse.sendRedirect("/search/genre=" + genre + "&page=1");
        }
        return null;
    }
}
