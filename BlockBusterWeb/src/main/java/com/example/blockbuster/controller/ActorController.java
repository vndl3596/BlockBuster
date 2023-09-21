package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.CastCall;
import com.example.blockbuster.apicall.MovieCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.CastDTO;
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
public class ActorController {
    LoginResponse loginResponse;
    int actId = 0;

    @RequestMapping("celebrity/actor/id={id}&page={page}")
    public ModelAndView actorDetail(HttpSession session,
                                    Model model,
                                    @PathVariable("id") int actor,
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
        CastDTO cast = CastCall.getCastById(actor);
        actId = cast.getId();
        ArrayList<MovieDTO> listMovieOnCast = MovieCall.getAllMovieOnCast(actor);

        ArrayList<MovieDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listMovieOnCast.size()) break;
            else showList.add(listMovieOnCast.get(i));
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

        float totalPageFloat = (float) listMovieOnCast.size() / pageNum;
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

        session.setAttribute("oldUrl", "/celebrity/actor/id=" + actor + "&page=" + page);
        model.addAttribute("format", format);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("mvNum", listMovieOnCast.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        model.addAttribute("cast", cast);
        return new ModelAndView("actor");
    }

    @RequestMapping("celebrity/actor/page-select/page={page}")
    public ModelAndView selectPageActor(HttpServletResponse httpServletResponse,
                                        @PathVariable("page") int page) throws IOException {
        httpServletResponse.sendRedirect("/celebrity/actor/id=" + actId + "&page=" + page);
        return null;
    }

}
