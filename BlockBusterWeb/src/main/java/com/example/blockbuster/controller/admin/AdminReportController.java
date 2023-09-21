package com.example.blockbuster.controller.admin;

import com.example.blockbuster.apicall.DataCall;
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
@RequestMapping("/admin/")
public class AdminReportController {
    ArrayList<GenreDTO> listAllGenre;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;
    ArrayList<CastDTO> listAllCast;
    ArrayList<DirectorDTO> listAllDir;
    ArrayList<MovieDTO> listAllMovie;
    Map<MovieDTO, Float> mapAllMovieWithRating = new TreeMap<>(new Comparator<MovieDTO>() {
        @Override
        public int compare(MovieDTO o1, MovieDTO o2) {
            if (o1.getId() < o2.getId())
                return 1;
            else return -1;
        }
    });

    @RequestMapping("report/mvg")
    public ModelAndView adminReportMovieOnGenre(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/report/mvg");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllGenre == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("genre", 0);
        model.addAttribute("format", format);
        model.addAttribute("listAllGenre", listAllGenre);
        return new ModelAndView("admin/movieongenre");
    }

    @RequestMapping("report/mvg/idGenre={id}")
    public ModelAndView adminReportMovieOnGenreShow(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/report/mvg/idGenre=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllGenre == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
        }
        ArrayList<MovieDTO> listMovieByGenre = new ArrayList<>();
        String uriGenreMovie = "http://localhost:8080/api/fkGenre/getAllMovie/" + id;
        ResponseEntity<MovieDTO[]> responseGenreMovie = restTemplate.getForEntity(uriGenreMovie, MovieDTO[].class);
        Collections.addAll(listMovieByGenre, responseGenreMovie.getBody());
        for (MovieDTO mv : listMovieByGenre) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("genre", id);
        model.addAttribute("format", format);
        model.addAttribute("listAllGenre", listAllGenre);
        model.addAttribute("listMovieByGenre", listMovieByGenre);
        return new ModelAndView("admin/movieongenre");
    }

    @RequestMapping("report/mva")
    public ModelAndView adminReportMovieOnActor(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/report/mva");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllCast == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("format", format);
        model.addAttribute("listAllCast", listAllCast);
        return new ModelAndView("admin/castforreport");
    }

    @RequestMapping("report/mva/idAct={id}")
    public ModelAndView adminReportMovieOnActorShow(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/report/mva/idAct=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllCast == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
        }

        CastDTO actor = new CastDTO();
        for (CastDTO c : listAllCast) {
            if (c.getId() == id) {
                actor = c;
            }
        }

        ArrayList<MovieDTO> listMovieOnCast = new ArrayList<>();
        String uriMovieOnCast = "http://localhost:8080/api/fkCast/movie/" + id;
        ResponseEntity<MovieDTO[]> responseMovieOnCast = restTemplate.getForEntity(uriMovieOnCast, MovieDTO[].class);
        Collections.addAll(listMovieOnCast, responseMovieOnCast.getBody());

        for (MovieDTO mv : listMovieOnCast) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("format", format);
        model.addAttribute("listAllCast", listAllCast);
        model.addAttribute("listMovieOnCast", listMovieOnCast);
        model.addAttribute("actor", actor);
        return new ModelAndView("admin/movieoncast");
    }

    @RequestMapping("report/mvd")
    public ModelAndView adminReportMovieOnDirector(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/report/mvd");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllDir == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("format", format);
        model.addAttribute("listAllDir", listAllDir);
        return new ModelAndView("admin/dirforreport");
    }

    @RequestMapping("report/mvd/idDir={id}")
    public ModelAndView adminReportMovieOnDirectorShow(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/report/mvd/idDir=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllDir == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
        }

        DirectorDTO dir = new DirectorDTO();
        for (DirectorDTO d : listAllDir) {
            if (d.getId() == id) {
                dir = d;
            }
        }

        ArrayList<MovieDTO> listMovieOnDir = new ArrayList<>();
        String uriMovieOnDir = "http://localhost:8080/api/fkDirector/movie/" + id;
        ResponseEntity<MovieDTO[]> responseMovieOnDir = restTemplate.getForEntity(uriMovieOnDir, MovieDTO[].class);
        Collections.addAll(listMovieOnDir, responseMovieOnDir.getBody());

        for (MovieDTO mv : listMovieOnDir) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("format", format);
        model.addAttribute("listAllCast", listAllCast);
        model.addAttribute("listMovieOnDir", listMovieOnDir);
        model.addAttribute("dir", dir);
        return new ModelAndView("admin/movieondir");
    }

    @RequestMapping("report/rating")
    public ModelAndView adminReportMovieRating(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/report/rating");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");

            for (MovieDTO mv : listAllMovie) {
                String uriMVRate = "http://localhost:8080/api/movieDetail/getMovieRate/" + mv.getId();
                ResponseEntity<MovieRateDTO> responseMVRate = restTemplate.getForEntity(uriMVRate, MovieRateDTO.class);
                mapAllMovieWithRating.put(mv, responseMVRate.getBody().getRate());
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("search", 0);
        model.addAttribute("format", format);
        return new ModelAndView("admin/movieforstar");
    }

    @RequestMapping(method = RequestMethod.POST, path = "report/rating")
    public ModelAndView adminReportMovieRatingShow(Model model, HttpSession session,
                                                   @RequestParam("min") float min,
                                                   @RequestParam("max") float max,
                                                   HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        DecimalFormat df = new DecimalFormat("#.#");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/report/rating");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");

            for (MovieDTO mv : listAllMovie) {
                String uriMVRate = "http://localhost:8080/api/movieDetail/getMovieRate/" + mv.getId();
                ResponseEntity<MovieRateDTO> responseMVRate = restTemplate.getForEntity(uriMVRate, MovieRateDTO.class);
                mapAllMovieWithRating.put(mv, responseMVRate.getBody().getRate());
            }
        }

        Map<MovieDTO, Float> mapMovieOnRating = new TreeMap<>(new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                if (o1.getId() < o2.getId())
                    return 1;
                else return -1;
            }
        });

        for (Map.Entry<MovieDTO, Float> fk : mapAllMovieWithRating.entrySet()) {
            if ((fk.getValue() >= min) && (fk.getValue() <= max)) {
                mapMovieOnRating.put(fk.getKey(), fk.getValue());
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("search", 1);
        model.addAttribute("format", format);
        model.addAttribute("mapMovieOnRating", mapMovieOnRating);
        model.addAttribute("df", df);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        return new ModelAndView("admin/movieforstar");
    }
}
