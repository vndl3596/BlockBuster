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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@RestController
@RequestMapping("/admin/")
public class AdminFkDirController {
    ArrayList<MovieDTO> listAllMovie;
    ArrayList<DirectorDTO> listAllDir;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("fkdir")
    public ModelAndView adminFKDirShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/fkdir");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
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
        return new ModelAndView("admin/movieforfkdir");
    }

    @RequestMapping("fkdir/idMov={id}")
    public ModelAndView adminFKDirOnMovieShow(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/fkdir/idMov=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
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

        ArrayList<DirectorDTO> listDirOnMovie = new ArrayList<>();
        String urlGetDirOnMovie = "http://localhost:8080/api/fkDirector/director/" + id;
        ResponseEntity<DirectorDTO[]> responseGetDirOnMovie = restTemplate.getForEntity(urlGetDirOnMovie, DirectorDTO[].class);
        Collections.addAll(listDirOnMovie, responseGetDirOnMovie.getBody());

        for (DirectorDTO dir : listDirOnMovie) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", dir.getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            dir.setAvatar(response.getBody().getUrl());
        }

        ArrayList<DirectorDTO> ListDirNotOnMovie = new ArrayList<>();
        ListDirNotOnMovie.addAll(listAllDir);
        for (DirectorDTO dir : listAllDir) {
            for (DirectorDTO fk : listDirOnMovie) {
                if (dir.getId() == fk.getId()) {
                    ListDirNotOnMovie.remove(fk);
                }
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("editMovie", editMovie);
        model.addAttribute("ListDirNotOnMovie", ListDirNotOnMovie);
        model.addAttribute("listDirOnMovie", listDirOnMovie);
        model.addAttribute("format", format);
        return new ModelAndView("admin/fkdir");
    }

    @RequestMapping("fkdir/add/idMov={idMv}&idDir={idDir}")
    public ModelAndView adminFKDirOnMovieAdd(Model model, HttpSession session,
                                             @PathVariable("idMv") int idMv,
                                             @PathVariable("idDir") int idDir,
                                             HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/fkdir/idMov=" + idMv);
            response.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        String urlAddDirForMovie = "http://localhost:8080/api/fkDirector/create/idMv=" + idMv + "&idD=" + idDir;
        restTemplate.getForEntity(urlAddDirForMovie, String.class);
        response.sendRedirect("/admin/fkdir/idMov=" + idMv);
        return null;
    }

    @RequestMapping("fkdir/delete/idMov={idMv}&idDir={idDir}")
    public ModelAndView adminFKDirOnMovieDelete(Model model, HttpSession session,
                                                @PathVariable("idMv") int idMv,
                                                @PathVariable("idDir") int idDir,
                                                HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/fkdir/idMov=" + idMv);
            response.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        String urlAddDirForMovie = "http://localhost:8080/api/fkDirector/remove/idMv=" + idMv + "&idD=" + idDir;
        restTemplate.getForEntity(urlAddDirForMovie, String.class);
        response.sendRedirect("/admin/fkdir/idMov=" + idMv);
        return null;
    }
}
