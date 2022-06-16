package com.example.blockbuster.controller;

import com.example.blockbuster.apiCall.DataCall;
import com.example.blockbuster.dto.*;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.http.*;
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
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/")
public class MovieDetailController {

    @RequestMapping("movie-detail/id={id}")
    public ModelAndView movieDetail(@PathVariable int id,
                                    Model model,
                                    HttpSession session) {
        DecimalFormat df = new DecimalFormat("#.#");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat evaFormatter = new SimpleDateFormat(" dd-MM-yyyy hh:mm:ss ");

        ArrayList<MovieDTO> listAllMovie;
        ArrayList<FKCastDTO> listFKCast;
        ArrayList<CastDTO> listAllCast;
        listFKCast = (ArrayList<FKCastDTO>) session.getAttribute("listFKCast");
        listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listFKCast = (ArrayList<FKCastDTO>) session.getAttribute("listFKCast");
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
        }

        RestTemplate restTemplate = new RestTemplate();
        Map<String, CastDTO> characterList = new HashMap<>();
        for (FKCastDTO fk: listFKCast) {
            if(fk.getMovieDetailId() == id) {
                for (CastDTO cast : listAllCast) {
                    if(fk.getMovieCastId() == cast.getId()) {
                        characterList.put(fk.getCastCharacter(), cast);
                    }
                }
            }
        }

        MovieDTO mv = new MovieDTO();
        for (MovieDTO movie : listAllMovie) {
            if (movie.getId() == id) {
                mv = movie;
                break;
            }
        }

        ArrayList<CastDTO> castList = new ArrayList<>();

        String uriAllCastOnMovie = "http://localhost:8080/api/fkCast/cast/" + id;
        ResponseEntity<CastDTO[]> responseAllCastOnMovie = restTemplate.getForEntity(uriAllCastOnMovie, CastDTO[].class);
        Collections.addAll(castList, responseAllCastOnMovie.getBody());

        ArrayList<DirectorDTO> directorList = new ArrayList<>();

        String uriAllDirectorOnMovie = "http://localhost:8080/api/fkDirector/director/" + id;
        ResponseEntity<DirectorDTO[]> responseAllDirectorOnMovie = restTemplate.getForEntity(uriAllDirectorOnMovie, DirectorDTO[].class);
        Collections.addAll(directorList, responseAllDirectorOnMovie.getBody());

        for (DirectorDTO d: directorList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", d.getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            d.setAvatar(response.getBody().getUrl());
        }

        ArrayList<GenreDTO> genreList = new ArrayList<>();

        String uriAllGenreOnMovie = "http://localhost:8080/api/fkGenre/getAllGenre/" + id;
        ResponseEntity<GenreDTO[]> responseAllGenreOnMovie = restTemplate.getForEntity(uriAllGenreOnMovie, GenreDTO[].class);
        Collections.addAll(genreList, responseAllGenreOnMovie.getBody());

        ArrayList<MovieEvaluateDTO> evaluateList = new ArrayList<>();

        String uriAllEvaluateOnMovie = "http://localhost:8080/api/movieDetail/loadEvaluateInMovie/" + id;
        ResponseEntity<MovieEvaluateDTO[]> responseAllEvaluateOnMovie = restTemplate.getForEntity(uriAllEvaluateOnMovie, MovieEvaluateDTO[].class);
        Collections.addAll(evaluateList, responseAllEvaluateOnMovie.getBody());
        Collections.sort(evaluateList, new Comparator<MovieEvaluateDTO>() {
            @Override
            public int compare(MovieEvaluateDTO o1, MovieEvaluateDTO o2) {
                return -o1.getEvaluateTime().compareTo(o2.getEvaluateTime());
            }
        });

        for (MovieEvaluateDTO me: evaluateList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", me.getAccId().getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            me.getAccId().setAvatar(response.getBody().getUrl());
        }

        ArrayList<MovieDTO> relateMovieListAll = new ArrayList<>();

        for (GenreDTO genreDTO : genreList) {
            String uriGenreMovie = "http://localhost:8080/api/fkGenre/getAllMovie/" + genreDTO.getId();
            ResponseEntity<MovieDTO[]> responseGenreMovie = restTemplate.getForEntity(uriGenreMovie, MovieDTO[].class);
            Collections.addAll(relateMovieListAll, responseGenreMovie.getBody());
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
        for (MovieDTO m: relateMovieList){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", m.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            m.setPoster(response.getBody().getUrl());
            String uriMVRate = "http://localhost:8080/api/movieDetail/getMovieRate/" + mv.getId();
            ResponseEntity<MovieRateDTO> responseMVRate = restTemplate.getForEntity(uriMVRate, MovieRateDTO.class);
            showMap.put(m, responseMVRate.getBody().getRate());
        }

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

        LoginResponse loginResponse;
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if(loginResponse != null){
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/movie-detail/id=" + id);
        model.addAttribute("loginResponse",loginResponse);
        model.addAttribute("rate", rate);
        model.addAttribute("starList", starList);
        model.addAttribute("evaluateList", evaluateList);
        model.addAttribute("characterList", characterList);
        model.addAttribute("evaFormatter",evaFormatter);
        model.addAttribute("format", formatter);
        model.addAttribute("df", df);
        model.addAttribute("movie", mv);
        model.addAttribute("castList", castList);
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
        LoginResponse loginResponse;
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if(loginResponse == null){
            session.setAttribute("oldUrl", "/movie-detail/id=" + id);
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");

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
            }
        }

        MovieEvaluateDTO mvE = new MovieEvaluateDTO(mv, loginAcc, new Date(), content, rate);
        HttpEntity<MovieEvaluateDTO> requestBody = new HttpEntity<>(mvE);
        String urlEvaluate = "http://localhost:8080/api/movieDetail/saveEvaluate";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MovieEvaluateDTO> responseEntity = restTemplate.exchange(urlEvaluate, HttpMethod.PUT, requestBody, MovieEvaluateDTO.class);
        response.sendRedirect("/movie-detail/id=" + id);
        return null;
    }

}
