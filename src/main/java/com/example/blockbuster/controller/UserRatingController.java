package com.example.blockbuster.controller;

import com.example.blockbuster.apiCall.DataCall;
import com.example.blockbuster.dto.*;
import com.example.blockbuster.dto.address.TownDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

@RestController
@RequestMapping("/")
public class UserRatingController {
    AccountDTO acc = new AccountDTO();
    ArrayList<MovieEvaluateDTO> list = new ArrayList<>();
    @RequestMapping(method = RequestMethod.GET, path = "user-rating/page={page}")
    public ModelAndView userRatingShow(Model model, HttpSession session, HttpServletResponse response, @PathVariable("page") int page) throws IOException {
        LoginResponse loginResponse;
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if(loginResponse == null){
            session.setAttribute("oldUrl", "/user-rating/page=" + page);
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");
        acc = loginAcc;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        int pageNum = 5;
        list.removeAll(list);
        RestTemplate restTemplate = new RestTemplate();
        String uriAllEvaluateOnUser = "http://localhost:8080/api/movieDetail/loadEvaluateInAccount/" + loginAcc.getId();
        ResponseEntity<MovieEvaluateDTO[]> responseAllEvaluateOnUser = restTemplate.getForEntity(uriAllEvaluateOnUser, MovieEvaluateDTO[].class);
        Collections.addAll(list, responseAllEvaluateOnUser.getBody());
        Collections.sort(list, new Comparator<MovieEvaluateDTO>() {
            @Override
            public int compare(MovieEvaluateDTO o1, MovieEvaluateDTO o2) {
                return -o1.getEvaluateTime().compareTo(o2.getEvaluateTime());
            }
        });

        ArrayList<MovieEvaluateDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == list.size()) break;
            else showList.add(list.get(i));
        }

        for (MovieEvaluateDTO me: showList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", me.getMovieId().getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> responseIMG = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            MovieDTO tmpMV = new MovieDTO(me.getMovieId().getId(), me.getMovieId().getTitle(), responseIMG.getBody().getUrl(), me.getMovieId().getDetail(), me.getMovieId().getMovieStatus(), me.getMovieId().getLinkTrailer(), me.getMovieId().getLinkMovie(), me.getMovieId().getReleaseDate(), me.getMovieId().getMovieDuration(), me.getMovieId().getViewNumber());
            me.setMovieId(tmpMV);
        }

        float totalPageFloat = (float) list.size() / pageNum;
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

        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("acc", acc);
        model.addAttribute("format", format);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showList);
        return new ModelAndView("userrate");
    }
    @RequestMapping("user-rating/page-select/page={page}")
    public ModelAndView selectPageUserRating(HttpServletResponse httpServletResponse,
                                              @PathVariable("page") int page) throws IOException {
        httpServletResponse.sendRedirect("/user-rating/page=" + page);
        return null;
    }
}
