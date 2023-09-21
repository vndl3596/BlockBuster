package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.*;
import com.example.blockbuster.dto.*;
import org.springframework.ui.Model;
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
public class MovieDetailController {
    LoginResponse loginResponse;

    @RequestMapping("movie-detail/id={id}")
    public ModelAndView movieDetail(@PathVariable int id,
                                    Model model,
                                    HttpSession session) {
        DecimalFormat df = new DecimalFormat("#.#");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat evaFormatter = new SimpleDateFormat(" dd-MM-yyyy hh:mm:ss ");
        ArrayList<MovieDTO> listAllMovie = MovieCall.getAllMovie();
        ArrayList<FKCastDTO> listFKCast = ForeignKeyCastMovieCall.getAllFKCastMovie();
        ArrayList<DirectorDTO> directorList = DirectorCall.getAllDirectorOnMovie(id);
        ArrayList<GenreDTO> genreList = GenreCall.getGenreByMovieId(id);
        ArrayList<MovieEvaluateDTO> evaluateList = EvaluateCall.getEvaluateByMovieId(id);

        RestTemplate restTemplate = new RestTemplate();
        Map<CastDTO, String> characterList = new HashMap<>();
        for (FKCastDTO fk : listFKCast) {
            if (fk.getMovieDetailId() == id) {
                characterList.put(CastCall.getCastById(fk.getMovieCastId()), fk.getCastCharacter());
            }
        }

        MovieDTO mv = new MovieDTO();
        for (MovieDTO movie : listAllMovie) {
            if (movie.getId() == id) {
                mv = movie;
                break;
            }
        }

        ArrayList<MovieDTO> relateMovieListAll = new ArrayList<>();
        for (GenreDTO genreDTO : genreList) {
            relateMovieListAll.addAll(MovieCall.getAllComingMovieOnGenre(genreDTO.getId()));
            relateMovieListAll.addAll(MovieCall.getAllShowingMovieOnGenre(genreDTO.getId()));
        }

        for (int i = 0; i < (relateMovieListAll.size() - 1); i++) {
            for (int j = i + 1; j < (relateMovieListAll.size()); j++) {
                if ((relateMovieListAll.get(i).getId() == relateMovieListAll.get(j).getId()) || (relateMovieListAll.get(j).getId() == id)) {
                    relateMovieListAll.remove(j);
                }
            }
        }

        Collections.shuffle(relateMovieListAll);
        ArrayList<MovieDTO> relateMovieList = new ArrayList<>();
        int size = 0;
        for (MovieDTO movie : relateMovieListAll) {
            if ((size == 5) || (size == relateMovieListAll.size())) break;
            relateMovieList.add(relateMovieListAll.get(size));
            size++;
        }

        Map<MovieDTO, Float> showMap = new HashMap<>();
        for (MovieDTO m : relateMovieList) {
            showMap.put(m, MovieCall.getMovieRate(m.getId()));
        }

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

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/movie-detail/id=" + id);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("rate", rate);
        model.addAttribute("starList", starList);
        model.addAttribute("evaluateList", evaluateList);
        model.addAttribute("characterList", characterList);
        model.addAttribute("evaFormatter", evaFormatter);
        model.addAttribute("format", formatter);
        model.addAttribute("df", df);
        model.addAttribute("movie", mv);
        model.addAttribute("directorList", directorList);
        model.addAttribute("genreList", genreList);
        model.addAttribute("relateMovieList", showMap);

        return new ModelAndView("moviesingle");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/movie-detail/evaluate/id={id}")
    public ModelAndView movieEvaluateGet(@PathVariable int id,
                                         HttpServletResponse response) throws IOException {
        response.sendRedirect("/movie-detail/id=" + id);
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/movie-detail/evaluate/id={id}")
    public ModelAndView movieEvaluatePost(@PathVariable int id,
                                          @RequestParam("content") String content,
                                          @RequestParam("rating") int rate,
                                          HttpSession session,
                                          HttpServletResponse response) throws IOException {
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/movie-detail/id=" + id);
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");
        ArrayList<MovieDTO> listAllMovie = MovieCall.getAllMovie();

        MovieDTO mv = new MovieDTO();
        for (MovieDTO movie : listAllMovie) {
            if (movie.getId() == id) {
                mv = movie;
            }
        }

        MovieEvaluateDTO mvE = new MovieEvaluateDTO(mv, loginAcc, new Date(), content, rate);
        EvaluateCall.evaluateMovie(mvE);
        response.sendRedirect("/movie-detail/id=" + id);
        return null;
    }

}
