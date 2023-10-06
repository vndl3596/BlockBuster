package com.example.blockbuster.controller.admin;

import com.example.blockbuster.apicall.DataCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.GenreDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.util.MessageUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/admin/")
public class AdminGenreController {
    ArrayList<GenreDTO> listAllGenre;
    Boolean isEdit = false;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("genre")
    public ModelAndView adminGenreShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/genre");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllGenre == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.reverse(listAllGenre);
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("format", format);
        model.addAttribute("listAllGenre", listAllGenre);
        return new ModelAndView("admin/genre");
    }

    @RequestMapping(method = RequestMethod.POST, path = "genre/add")
    public ModelAndView adminAddGenre(Model model, HttpSession session, @RequestParam(value = "genreName") String genreName, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        boolean check = true;
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/genre");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllGenre == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.reverse(listAllGenre);
        }

        for (GenreDTO genre : listAllGenre) {
            if (genre.getName().equals(genreName)) {
                check = false;
                break;
            }
        }

        if (check == true) {
            err += MessageUtil.VALIDATION_GENRE_ADD_SUCCESS;
            String urlAddGenre = "http://localhost:8080/api/genre/add";
            GenreDTO genreDTO = new GenreDTO(0, genreName);
            HttpEntity<GenreDTO> requestAddGenre = new HttpEntity<>(genreDTO);
            ResponseEntity<GenreDTO> responseAddGenre = restTemplate.postForEntity(urlAddGenre, requestAddGenre, GenreDTO.class);
            listAllGenre.add(0, responseAddGenre.getBody());
        } else err += MessageUtil.VALIDATION_GENRE_AU_ERR01;

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("listAllGenre", listAllGenre);

        return new ModelAndView("admin/genre");
    }

    @RequestMapping(path = "genre/delete/idGenre={id}")
    public ModelAndView adminDeleteGenre(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/genre");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllGenre == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.reverse(listAllGenre);
        }

        err += MessageUtil.VALIDATION_GENRE_DELETE_SUCCESS;
        String urlDeleteGenre = "http://localhost:8080/api/genre/remove/" + id;
        restTemplate.getForEntity(urlDeleteGenre, String.class);
        for (GenreDTO genre : listAllGenre) {
            if (genre.getId() == id) {
                listAllGenre.remove(genre);
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("listAllGenre", listAllGenre);

        return new ModelAndView("admin/genre");
    }

    @RequestMapping(path = "genre/edit/idGenre={id}")
    public ModelAndView adminEditGenreShow(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = true;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/genre/edit/idGenre=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        GenreDTO genreEdit = new GenreDTO();
        if (listAllGenre == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.reverse(listAllGenre);
        }

        for (GenreDTO genre : listAllGenre) {
            if (genre.getId() == id) {
                genreEdit = genre;
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("genreEdit", genreEdit);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("listAllGenre", listAllGenre);

        return new ModelAndView("admin/genre");
    }

    @RequestMapping(method = RequestMethod.POST, path = "genre/edit/idGenre={id}")
    public ModelAndView adminEditGenrePost(Model model, HttpSession session, @PathVariable(value = "id") int id, @RequestParam(value = "genreName") String genreName, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = true;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/genre/edit/idGenre=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        boolean check = true;
        GenreDTO genreEdit = new GenreDTO();
        if (listAllGenre == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.reverse(listAllGenre);
        }

        for (GenreDTO genre : listAllGenre) {
            if (genre.getId() == id) {
                genreEdit = genre;
                break;
            }
        }

        for (GenreDTO genre : listAllGenre) {
            if ((genre.getId() != id) && (genre.getName().equals(genreName))) {
                check = false;
                break;
            }
        }

        if (check == true) {
            err += MessageUtil.VALIDATION_GENRE_UPDATE_SUCCESS;
            String urlAddGenre = "http://localhost:8080/api/genre/edit";
            GenreDTO genreDTO = new GenreDTO(id, genreName);
            HttpEntity<GenreDTO> requestEditGenre = new HttpEntity<>(genreDTO);
            restTemplate.postForEntity(urlAddGenre, requestEditGenre, GenreDTO.class);
            listAllGenre.set(listAllGenre.indexOf(genreEdit), genreDTO);
            genreEdit.setName(genreName);
        } else err += MessageUtil.VALIDATION_GENRE_AU_ERR01;

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("genreEdit", genreEdit);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("listAllGenre", listAllGenre);

        return new ModelAndView("admin/genre");
    }
}
