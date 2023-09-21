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
public class AdminEvaluateController {
    boolean byMv = false;
    boolean byAcc = false;

    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;
    ArrayList<MovieDTO> listAllMovie;
    ArrayList<AccountDTO> listAllAccount;

    @RequestMapping("evaluate/bymv")
    public ModelAndView adminEvaluateOnMovie(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();
        byMv = true;
        byAcc = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/evaluate/bymv");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/movieforevaluate");
    }

    @RequestMapping("evaluate/bymv/idMov={id}")
    public ModelAndView adminEvaluateOnMovieShow(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat(" dd-MM-yyyy hh:mm:ss ");
        RestTemplate restTemplate = new RestTemplate();
        byMv = true;
        byAcc = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/evaluate/bymv/idMov=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        }

        MovieDTO mv = new MovieDTO();
        for (MovieDTO m : listAllMovie) {
            if (m.getId() == id) {
                mv = m;
            }
        }

        ArrayList<MovieEvaluateDTO> listEvaluateOnMovie = new ArrayList<>();
        String uriAllEvaluateOnMovie = "http://localhost:8080/api/movieDetail/loadEvaluateInMovie/" + id;
        ResponseEntity<MovieEvaluateDTO[]> responseAllEvaluateOnMovie = restTemplate.getForEntity(uriAllEvaluateOnMovie, MovieEvaluateDTO[].class);
        Collections.addAll(listEvaluateOnMovie, responseAllEvaluateOnMovie.getBody());
        Collections.sort(listEvaluateOnMovie, new Comparator<MovieEvaluateDTO>() {
            @Override
            public int compare(MovieEvaluateDTO o1, MovieEvaluateDTO o2) {
                return -o1.getEvaluateTime().compareTo(o2.getEvaluateTime());
            }
        });

        for (MovieEvaluateDTO me : listEvaluateOnMovie) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", me.getAccId().getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            if (!me.getAccId().getAvatar().equals("")) {
                me.getAccId().setAvatar(response.getBody().getUrl());
            } else me.getAccId().setAvatar("/images/uploads/user.png");
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("mv", mv);
        model.addAttribute("listEvaluateOnMovie", listEvaluateOnMovie);
        model.addAttribute("format", format);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/evaluateonmovie");
    }

    @RequestMapping("evaluate/byac")
    public ModelAndView adminEvaluateOnAccount(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/evaluate/byac");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        byMv = false;
        byAcc = true;

        if (listAllAccount == null) {
            listAllAccount = new ArrayList<>();
            String urlGetAllAccount = "http://localhost:8080/api/acc/getAllAccount";
            ResponseEntity<AccountDTO[]> reponseGetAllAccount = restTemplate.getForEntity(urlGetAllAccount, AccountDTO[].class);
            Collections.addAll(listAllAccount, reponseGetAllAccount.getBody());
            Collections.sort(listAllAccount, new Comparator<AccountDTO>() {
                @Override
                public int compare(AccountDTO o1, AccountDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
            for (AccountDTO acc : listAllAccount) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", acc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                acc.setAvatar(response.getBody().getUrl());
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("listAllAccount", listAllAccount);
        return new ModelAndView("admin/accountforevaluate");
    }

    @RequestMapping("evaluate/byac/idAcc={id}")
    public ModelAndView adminEvaluateOnAccountShow(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat(" dd-MM-yyyy hh:mm:ss ");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/evaluate/byac/idAcc=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        byMv = false;
        byAcc = true;

        if (listAllAccount == null) {
            listAllAccount = new ArrayList<>();
            String urlGetAllAccount = "http://localhost:8080/api/acc/getAllAccount";
            ResponseEntity<AccountDTO[]> reponseGetAllAccount = restTemplate.getForEntity(urlGetAllAccount, AccountDTO[].class);
            Collections.addAll(listAllAccount, reponseGetAllAccount.getBody());
            Collections.sort(listAllAccount, new Comparator<AccountDTO>() {
                @Override
                public int compare(AccountDTO o1, AccountDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
            for (AccountDTO acc : listAllAccount) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", acc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                acc.setAvatar(response.getBody().getUrl());
            }
        }

        AccountDTO acc = new AccountDTO();
        for (AccountDTO a : listAllAccount) {
            if (a.getId() == id) {
                acc = a;
            }
        }

        ArrayList<MovieEvaluateDTO> listEvaluateOnAccount = new ArrayList<>();
        String uriAllEvaluateOnAccount = "http://localhost:8080/api/movieDetail/loadEvaluateInAccount/" + id;
        ResponseEntity<MovieEvaluateDTO[]> responseAllEvaluateOnAccount = restTemplate.getForEntity(uriAllEvaluateOnAccount, MovieEvaluateDTO[].class);
        Collections.addAll(listEvaluateOnAccount, responseAllEvaluateOnAccount.getBody());
        Collections.sort(listEvaluateOnAccount, new Comparator<MovieEvaluateDTO>() {
            @Override
            public int compare(MovieEvaluateDTO o1, MovieEvaluateDTO o2) {
                return -o1.getEvaluateTime().compareTo(o2.getEvaluateTime());
            }
        });

        for (MovieEvaluateDTO me : listEvaluateOnAccount) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", me.getMovieId().getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            if (!me.getMovieId().getPoster().equals("")) {
                me.getMovieId().setPoster(response.getBody().getUrl());
            } else me.getMovieId().setPoster("/images/uploads/user.png");
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("acc", acc);
        model.addAttribute("listEvaluateOnAccount", listEvaluateOnAccount);
        model.addAttribute("format", format);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/evaluateonaccount");
    }

    @RequestMapping("evaluate/disable/idAcc={idAcc}&idMv={idMv}")
    public ModelAndView adminDisableEvaluate(HttpServletResponse response, Model model, HttpSession session,
                                             @PathVariable("idMv") int idMv,
                                             @PathVariable("idAcc") int idAcc) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            if ((byAcc == false) && (byMv == false)) {
                session.setAttribute("oldAdminUrl", "/admin/evaluate/bymv");
            }

            if ((byAcc == true) && (byMv == false)) {
                session.setAttribute("oldAdminUrl", "/admin/evaluate/byac/idAcc=" + idAcc);
            }

            if ((byAcc == false) && (byMv == true)) {
                session.setAttribute("oldAdminUrl", "/admin/evaluate/bymv/idMov=" + idMv);
            }
            response.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String urlDisableEva = "http://localhost:8080/api/movieDetail/disableEvaluate/idMv=" + idMv + "&idA=" + idAcc;
        restTemplate.getForEntity(urlDisableEva, MovieEvaluateDTO.class);

        if ((byAcc == false) && (byMv == false)) {
            response.sendRedirect("/admin/evaluate/bymv");
        }

        if ((byAcc == true) && (byMv == false)) {
            response.sendRedirect("/admin/evaluate/byac/idAcc=" + idAcc);
        }

        if ((byAcc == false) && (byMv == true)) {
            response.sendRedirect("/admin/evaluate/bymv/idMov=" + idMv);
        }

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        }

        return null;
    }
}
