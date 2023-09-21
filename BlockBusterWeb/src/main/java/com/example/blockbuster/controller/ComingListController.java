package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.GenreCall;
import com.example.blockbuster.apicall.MovieCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.GenreDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MovieDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/")
public class ComingListController {

    ArrayList<MovieDTO> listAllMovieComing;
    ArrayList<MovieDTO> listMovieComingByGenre = new ArrayList<>();
    ArrayList<GenreDTO> listGenre;
    LoginResponse loginResponse;
    int genreSelect = 0;

    @RequestMapping("movie-grid/coming/page={page}")
    public ModelAndView allComingMovieGrid(HttpSession session,
                                           Model model,
                                           @PathVariable("page") int page) {
        DecimalFormat df = new DecimalFormat("#.#");
        genreSelect = 0;
        int pageNum = 20;

        ArrayList<MovieDTO> listAllMovieComing = MovieCall.getAllComingMovie();
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();

        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listAllMovieComing.size()) break;
            else showList.add(listAllMovieComing.get(i));
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

        float totalPageFloat = (float) listAllMovieComing.size() / pageNum;
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

        session.setAttribute("oldUrl", "/movie-grid/coming/page=" + page);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", listAllMovieComing.size());
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
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();
        ArrayList<MovieDTO> listMovieComingByGenre = MovieCall.getAllComingMovieOnGenre(idGenre);

        GenreDTO gen = new GenreDTO();
        for (GenreDTO g : listGenre) {
            if (g.getId() == idGenre) {
                gen = g;
            }
        }

        int pageNum = 20;
        ArrayList<MovieDTO> showList = new ArrayList<>();

        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listMovieComingByGenre.size()) break;
            else showList.add(listMovieComingByGenre.get(i));
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

        float totalPageFloat = (float) listMovieComingByGenre.size() / pageNum;
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
        model.addAttribute("gen", gen);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", listMovieComingByGenre.size());
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
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();
        ArrayList<MovieDTO> listAllMovieComing = MovieCall.getAllComingMovie();

        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listAllMovieComing.size()) break;
            else showList.add(listAllMovieComing.get(i));
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

        float totalPageFloat = (float) listAllMovieComing.size() / pageNum;
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
        model.addAttribute("mvNum", listAllMovieComing.size());
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
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();
        ArrayList<MovieDTO> listMovieComingByGenre = MovieCall.getAllComingMovieOnGenre(idGenre);

        GenreDTO gen = new GenreDTO();
        for (GenreDTO g : listGenre) {
            if (g.getId() == idGenre) {
                gen = g;
            }
        }

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        ArrayList<MovieDTO> tmpList = new ArrayList<>();

        int pageNum = 5;
        ArrayList<MovieDTO> showList = new ArrayList<>();

        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listMovieComingByGenre.size()) break;
            else showList.add(listMovieComingByGenre.get(i));
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

        float totalPageFloat = (float) listMovieComingByGenre.size() / pageNum;
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
        model.addAttribute("gen", gen);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("format", format);
        model.addAttribute("mvNum", listMovieComingByGenre.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("genreSelect", genreSelect);

        return new ModelAndView("cominglist");
    }
}
