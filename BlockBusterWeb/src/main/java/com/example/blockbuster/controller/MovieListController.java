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
public class MovieListController {
    LoginResponse loginResponse;
    int genreSelect = 0;

    @RequestMapping("movie-grid/showing/page={page}")
    public ModelAndView allMovieShowingGrid(HttpSession session,
                                            Model model,
                                            @PathVariable("page") int page) {
        DecimalFormat df = new DecimalFormat("#.#");
        genreSelect = 0;
        int pageNum = 20;
        ArrayList<MovieDTO> listAllMovieShowing = MovieCall.getAllShowingMovie();
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();

        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listAllMovieShowing.size()) break;
            else showList.add(listAllMovieShowing.get(i));
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

        float totalPageFloat = (float) listAllMovieShowing.size() / pageNum;
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

        session.setAttribute("oldUrl", "/movie-grid/showing/page=" + page);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", listAllMovieShowing.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("genreSelect", genreSelect);
        return new ModelAndView("moviegrid");
    }

    @RequestMapping(method = RequestMethod.POST, value = "movie-grid/showing/select-genre")
    public ModelAndView genreSelectGrid(HttpServletResponse httpServletResponse,
                                        @RequestParam(value = "genreS", required = true) int genre) throws IOException {
        genreSelect = genre;
        if (genre == 0) {
            httpServletResponse.sendRedirect("/movie-grid/showing/page=1");
        } else {
            httpServletResponse.sendRedirect("/movie-grid/showing/genre=" + genre + "&page=1");
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "movie-grid/showing/genre={id}&page={page}")
    public ModelAndView genreMovieGrid(@PathVariable("id") int idGenre,
                                       @PathVariable("page") int page,
                                       Model model,
                                       HttpSession session) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        genreSelect = idGenre;
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();
        ArrayList<MovieDTO> listMovieShowingByGenre = MovieCall.getAllShowingMovieOnGenre(idGenre);

        GenreDTO gen = new GenreDTO();
        for (GenreDTO g : listGenre) {
            if (g.getId() == idGenre) {
                gen = g;
            }
        }

        int pageNum = 20;
        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listMovieShowingByGenre.size()) break;
            else showList.add(listMovieShowingByGenre.get(i));
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

        float totalPageFloat = (float) listMovieShowingByGenre.size() / pageNum;
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

        session.setAttribute("oldUrl", "/movie-grid/showing/genre=" + idGenre + "&page=" + page);
        model.addAttribute("gen", gen);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", listMovieShowingByGenre.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("genreSelect", genreSelect);

        return new ModelAndView("moviegrid");
    }

    @RequestMapping("movie-grid/transfer")
    public ModelAndView transferGridToList(HttpServletResponse httpServletResponse) throws IOException {
        if (genreSelect == 0)
            httpServletResponse.sendRedirect("/movie-list/showing/page=1");
        else
            httpServletResponse.sendRedirect("/movie-list/showing/genre=" + genreSelect + "&page=1");
        return null;
    }

    @RequestMapping("movie-grid/showing/page-select/page={page}")
    public ModelAndView selectPageGridShowing(HttpServletResponse httpServletResponse,
                                              @PathVariable("page") int page) throws IOException {
        if (genreSelect == 0)
            httpServletResponse.sendRedirect("/movie-grid/showing/page=" + page);
        else
            httpServletResponse.sendRedirect("/movie-grid/showing/genre=" + genreSelect + "&page=" + page);
        return null;
    }

    @RequestMapping("movie-list/showing/page-select/page={page}")
    public ModelAndView selectPageListShowing(HttpServletResponse httpServletResponse,
                                              @PathVariable("page") int page) throws IOException {
        if (genreSelect == 0)
            httpServletResponse.sendRedirect("/movie-list/showing/page=" + page);
        else
            httpServletResponse.sendRedirect("/movie-list/showing/genre=" + genreSelect + "&page=" + page);
        return null;
    }

    @RequestMapping("movie-list/transfer")
    public ModelAndView transferListToGrid(HttpServletResponse httpServletResponse) throws IOException {
        if (genreSelect == 0)
            httpServletResponse.sendRedirect("/movie-grid/showing/page=1");
        else
            httpServletResponse.sendRedirect("/movie-grid/showing/genre=" + genreSelect + "&page=1");
        return null;
    }

    @RequestMapping("movie-list/showing/page={page}")
    public ModelAndView allMovieShowingList(HttpSession session,
                                            Model model,
                                            @PathVariable("page") int page) {
        DecimalFormat df = new DecimalFormat("#.#");
        genreSelect = 0;
        int pageNum = 5;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();
        ArrayList<MovieDTO> listAllMovieShowing = MovieCall.getAllShowingMovie();

        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listAllMovieShowing.size()) break;
            else showList.add(listAllMovieShowing.get(i));
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

        float totalPageFloat = (float) listAllMovieShowing.size() / pageNum;
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

        session.setAttribute("oldUrl", "/movie-list/showing/page=" + page);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("format", format);
        model.addAttribute("mvNum", listAllMovieShowing.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("genreSelect", genreSelect);
        return new ModelAndView("movielist");
    }

    @RequestMapping(method = RequestMethod.POST, value = "movie-list/showing/select-genre")
    public ModelAndView genreSelectList(HttpServletResponse httpServletResponse,
                                        @RequestParam(value = "genreS", required = true) int genre) throws IOException {
        genreSelect = genre;
        if (genre == 0) {
            httpServletResponse.sendRedirect("/movie-list/showing/page=1");
        } else {
            httpServletResponse.sendRedirect("/movie-list/showing/genre=" + genre + "&page=1");
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "movie-list/showing/genre={id}&page={page}")
    public ModelAndView genreMovieList(@PathVariable("id") int idGenre,
                                       @PathVariable("page") int page,
                                       Model model,
                                       HttpSession session) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        DecimalFormat df = new DecimalFormat("#.#");
        ArrayList<MovieDTO> listMovieShowingByGenre = MovieCall.getAllShowingMovieOnGenre(idGenre);
        ArrayList<GenreDTO> listGenre = GenreCall.getAllGenre();

        GenreDTO gen = new GenreDTO();
        for (GenreDTO g : listGenre) {
            if (g.getId() == idGenre) {
                gen = g;
            }
        }


        int pageNum = 5;
        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listMovieShowingByGenre.size()) break;
            else showList.add(listMovieShowingByGenre.get(i));
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

        float totalPageFloat = (float) listMovieShowingByGenre.size() / pageNum;
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

        session.setAttribute("oldUrl", "/movie-list/showing/genre=" + idGenre + "&page=" + page);
        model.addAttribute("gen", gen);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("format", format);
        model.addAttribute("mvNum", listMovieShowingByGenre.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("genreSelect", genreSelect);

        return new ModelAndView("movielist");
    }
}
