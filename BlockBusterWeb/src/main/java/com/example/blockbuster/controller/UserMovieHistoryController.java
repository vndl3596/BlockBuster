package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.MovieCall;
import com.example.blockbuster.apicall.MovieHistoryOfUserCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MovieDTO;
import com.example.blockbuster.dto.UserHistoryDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/")
public class UserMovieHistoryController {
    LoginResponse loginResponse;

    @RequestMapping(method = RequestMethod.GET, path = "user-history/page={page}")
    public ModelAndView userRatingShow(Model model, HttpSession session, HttpServletResponse response, @PathVariable("page") int page) throws IOException {
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/user-history/page=" + page);
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO acc = (AccountDTO) session.getAttribute("loginAcc");


        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        int pageNum = 5;
        ArrayList<UserHistoryDTO> history = MovieHistoryOfUserCall.getAllMovieHistoryOnUser(acc.getId());
        ArrayList<MovieDTO> listMovieHistory = new ArrayList<>();

        for (UserHistoryDTO h : history) {
            listMovieHistory.add(MovieCall.getMovieById(h.getMovie()));
        }

        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listMovieHistory.size()) break;
            else showList.add(listMovieHistory.get(i));
        }

        Map<UserHistoryDTO, MovieDTO> showMap = new TreeMap<>(new Comparator<UserHistoryDTO>() {
            @Override
            public int compare(UserHistoryDTO o1, UserHistoryDTO o2) {
                int compare = -o1.getHistoryDate().compareTo(o2.getHistoryDate());
                if (compare == 0) {
                    return 1;
                } else return compare;
            }
        });
        for (MovieDTO mv : showList) {
            for (UserHistoryDTO h : history) {
                if (mv.getId() == h.getMovie()) showMap.put(h, mv);
            }
        }

        float totalPageFloat = (float) listMovieHistory.size() / pageNum;
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
        model.addAttribute("mvNum", listMovieHistory.size());
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
