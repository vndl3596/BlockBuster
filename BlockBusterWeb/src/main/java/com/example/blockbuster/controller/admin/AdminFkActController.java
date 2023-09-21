package com.example.blockbuster.controller.admin;

import com.example.blockbuster.apicall.DataCall;
import com.example.blockbuster.dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/admin/")
public class AdminFkActController {
    ArrayList<MovieDTO> listAllMovie;
    ArrayList<CastDTO> listAllAct;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("fkact")
    public ModelAndView adminFKActShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/fkact");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listAllAct = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/movieforfkact");
    }

    @RequestMapping("fkact/idMov={id}")
    public ModelAndView adminFKActOnMovieShow(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/fkact/idMov=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listAllAct = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        MovieDTO editMovie = new MovieDTO();
        for (MovieDTO mv : listAllMovie) {
            if (mv.getId() == id) {
                editMovie = mv;
            }
        }

        Map<CastDTO, String> mapActOnMovie = new TreeMap<>(new Comparator<CastDTO>() {
            @Override
            public int compare(CastDTO o1, CastDTO o2) {
                if (o1.getId() < o2.getId())
                    return 1;
                else return -1;
            }
        });
        String urlGetActOnMovie = "http://localhost:8080/api/fkCast/cast/" + id;
        ResponseEntity<FKCastDTO[]> responseGetActOnMovie = restTemplate.getForEntity(urlGetActOnMovie, FKCastDTO[].class);

        for (FKCastDTO fk : responseGetActOnMovie.getBody()) {
            for (CastDTO cast : listAllAct) {
                if (fk.getMovieCastId() == cast.getId()) {
                    mapActOnMovie.put(cast, fk.getCastCharacter());
                }
            }
        }

        ArrayList<CastDTO> listActNotOnMovie = new ArrayList<>();
        listActNotOnMovie.addAll(listAllAct);
        boolean check = false;
        for (CastDTO act : listAllAct) {
            for (Map.Entry<CastDTO, String> fk : mapActOnMovie.entrySet()) {
                if (act.getId() == fk.getKey().getId()) {
                    listActNotOnMovie.remove(act);
                }
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("editMovie", editMovie);
        model.addAttribute("listActNotOnMovie", listActNotOnMovie);
        model.addAttribute("listActOnMovie", mapActOnMovie);
        model.addAttribute("format", format);
        return new ModelAndView("admin/fkact");
    }

    @RequestMapping("fkact/add/idMov={idMv}&idAct={idAct}")
    public ModelAndView adminFKActOnMovieAdd(Model model, HttpSession session,
                                             @PathVariable("idMv") int idMv,
                                             @PathVariable("idAct") int idAct,
                                             @RequestParam("character") String character,
                                             HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/fkact/idMov=" + idMv);
            response.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listAllAct = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        HttpEntity<String> requestBody = new HttpEntity<>(character);
        String urlAddActForMovie = "http://localhost:8080/api/fkCast/create/idMv=" + idMv + "&idA=" + idAct;
        restTemplate.postForEntity(urlAddActForMovie, requestBody, String.class);
        response.sendRedirect("/admin/fkact/idMov=" + idMv);
        return null;
    }

    @RequestMapping("fkact/edit/idMov={idMv}&idAct={idAct}")
    public ModelAndView adminFKActOnMovieEdit(Model model, HttpSession session,
                                              @PathVariable("idMv") int idMv,
                                              @PathVariable("idAct") int idAct,
                                              @RequestParam("character") String character,
                                              HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/fkact/idMov=" + idMv);
            response.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listAllAct = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        HttpEntity<String> requestBody = new HttpEntity<>(character);
        String urlAddActForMovie = "http://localhost:8080/api/fkCast/edit/idMv=" + idMv + "&idA=" + idAct;
        restTemplate.postForEntity(urlAddActForMovie, requestBody, String.class);
        response.sendRedirect("/admin/fkact/idMov=" + idMv);
        return null;
    }

    @RequestMapping("fkact/delete/idMov={idMv}&idAct={idAct}")
    public ModelAndView adminFKActOnMovieDelete(Model model, HttpSession session,
                                                @PathVariable("idMv") int idMv,
                                                @PathVariable("idAct") int idAct,
                                                HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/fkact/idMov=" + idMv);
            response.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listAllAct = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        String urlAddDirForMovie = "http://localhost:8080/api/fkCast/remove/idMv=" + idMv + "&idA=" + idAct;
        restTemplate.getForEntity(urlAddDirForMovie, String.class);
        response.sendRedirect("/admin/fkact/idMov=" + idMv);
        return null;
    }
}
