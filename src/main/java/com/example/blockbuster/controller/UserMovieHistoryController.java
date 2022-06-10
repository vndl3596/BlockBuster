package com.example.blockbuster.controller;

import com.example.blockbuster.apiCall.DataCall;
import com.example.blockbuster.dto.*;
import com.example.blockbuster.dto.address.TownDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
import java.util.*;

@RestController
@RequestMapping("/")
public class UserMovieHistoryController {
    AccountDTO acc = new AccountDTO();
    ArrayList<MovieDTO> list = new ArrayList<>();
    ArrayList<UserHistoryDTO> history = new ArrayList<>();
    @RequestMapping(method = RequestMethod.GET, path = "user-history/page={page}")
    public ModelAndView userRatingShow(Model model, HttpSession session, HttpServletResponse response, @PathVariable("page") int page) throws IOException {
        LoginResponse loginResponse;
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if(loginResponse == null){
            session.setAttribute("oldUrl", "/user-history/page=" + page);
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");
        acc = loginAcc;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        int pageNum = 5;
        list.removeAll(list);
        history.removeAll(history);
        RestTemplate restTemplate = new RestTemplate();
        String uriAllMovieOnUser = "http://localhost:8080/api/history/get-all-by-account/" + loginAcc.getId();
        ResponseEntity<UserHistoryDTO[]> responseAllMovieOnUser = restTemplate.getForEntity(uriAllMovieOnUser, UserHistoryDTO[].class);
        Collections.addAll(history, responseAllMovieOnUser.getBody());

        ArrayList<MovieDTO> listAllMV = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        if(listAllMV == null){
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMV = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
        }

        for (UserHistoryDTO h: history) {
                for (MovieDTO mv: listAllMV) {
                    if(mv.getId() == h.getMovie()) list.add(mv);
                }
        }

        ArrayList<MovieDTO> showList = new ArrayList<>();

        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == list.size()) break;
            else showList.add(list.get(i));
        }

        Map<UserHistoryDTO, MovieDTO> showMap = new TreeMap<>(new Comparator<UserHistoryDTO>() {
            @Override
            public int compare(UserHistoryDTO o1, UserHistoryDTO o2) {
                int compare = -o1.getHistoryDate().compareTo(o2.getHistoryDate());
                if(compare == 0){
                    return 1;
                }
                else return compare;
            }
        });
        for (MovieDTO mv: showList){
            for (UserHistoryDTO h: history) {
                if(mv.getId() == h.getMovie()) showMap.put(h, mv);
            }
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
        model.addAttribute("formater", formater);
        model.addAttribute("mvNum", list.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        return new ModelAndView("userhistory");
    }
    @RequestMapping("user-history/page-select/page={page}")
    public ModelAndView selectPageUserHistory(HttpServletResponse httpServletResponse,
                                             @PathVariable("page") int page) throws IOException {
        httpServletResponse.sendRedirect("/user-history/page=" + page);
        return null;
    }
}
