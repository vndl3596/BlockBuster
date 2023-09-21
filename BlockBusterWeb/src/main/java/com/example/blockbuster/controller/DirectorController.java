package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.DirectorCall;
import com.example.blockbuster.apicall.MovieCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.DirectorDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MovieDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/")
public class DirectorController {
    LoginResponse loginResponse;
    int dirId = 0;

    @RequestMapping("celebrity/director/id={id}&page={page}")
    public ModelAndView directorDetail(HttpSession session,
                                       Model model,
                                       @PathVariable("id") int dir,
                                       @PathVariable("page") int page) {
        DecimalFormat df = new DecimalFormat("#.#");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        int pageNum = 10;

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");

            model.addAttribute("loginAcc", loginAcc);
        }

        DirectorDTO cast = DirectorCall.getDirectorById(dir);
        dirId = cast.getId();
        ArrayList<MovieDTO> listMovieOnDir = MovieCall.getAllMovieOnDirector(dir);

        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listMovieOnDir.size()) break;
            else showList.add(listMovieOnDir.get(i));
        }
        Map<MovieDTO, Float> showMap = new TreeMap<>(new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });

        for (MovieDTO mv : showList) {
            showMap.put(mv, MovieCall.getMovieRate(mv.getId()));
        }

        float totalPageFloat = (float) listMovieOnDir.size() / pageNum;
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

        session.setAttribute("oldUrl", "/celebrity/actor/id=" + dir + "&page=" + page);
        model.addAttribute("format", format);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", listMovieOnDir.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("cast", cast);
        return new ModelAndView("director");
    }

    @RequestMapping("celebrity/director/page-select/page={page}")
    public ModelAndView selectPageDirector(HttpServletResponse httpServletResponse,
                                           @PathVariable("page") int page) throws IOException {
        httpServletResponse.sendRedirect("/celebrity/director/id=" + dirId + "&page=" + page);
        return null;
    }

}
