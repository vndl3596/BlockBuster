package com.example.blockbuster.controller;

import com.example.blockbuster.apicall.MovieCall;
import com.example.blockbuster.apicall.MovieHistoryOfUserCall;
import com.example.blockbuster.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class UserBuyHistoryController {
    LoginResponse loginResponse;

    @RequestMapping(method = RequestMethod.GET, path = "user-buy-history/page={page}")
    public ModelAndView userBuyHisShow(Model model, HttpSession session, HttpServletResponse response, @PathVariable("page") int page) throws IOException {
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/user-buy-history/page=" + page);
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO acc = (AccountDTO) session.getAttribute("loginAcc");


        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        int pageNum = 5;
        String urlGetAllBuyHisByUsername = "http://localhost:8080/api/membership-buy-his/getAll/" + acc.getUsername();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MembershipBuyHistoryDTO[]> responseGetAllBuyHisByUsername = restTemplate.getForEntity(urlGetAllBuyHisByUsername, MembershipBuyHistoryDTO[].class);
        ArrayList<MembershipBuyHistoryDTO> listAllBuyHisByUsername = new ArrayList<>();
        Collections.addAll(listAllBuyHisByUsername, responseGetAllBuyHisByUsername.getBody());
        Collections.sort(listAllBuyHisByUsername, new Comparator<MembershipBuyHistoryDTO>() {
            @Override
            public int compare(MembershipBuyHistoryDTO o1, MembershipBuyHistoryDTO o2) {
                int compare = -o1.getBuyTime().compareTo(o2.getBuyTime());
                if (compare == 0) {
                    return 1;
                } else return compare;
            }
        });

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        ArrayList<MembershipBuyHistoryDTO> showList = new ArrayList<>();
        for (int i = pageNum * (page - 1); i < pageNum * page; i++) {
            if (i == listAllBuyHisByUsername.size()) break;
            else showList.add(listAllBuyHisByUsername.get(i));
        }

        Map<MembershipBuyHistoryDTO, MembershipDTO> showMap = new TreeMap<>(new Comparator<MembershipBuyHistoryDTO>() {
            @Override
            public int compare(MembershipBuyHistoryDTO o1, MembershipBuyHistoryDTO o2) {
                int compare = -o1.getBuyTime().compareTo(o2.getBuyTime());
                if (compare == 0) {
                    return 1;
                } else return compare;
            }
        });
        for (MembershipBuyHistoryDTO mb : showList) {
            for (MembershipDTO m : listAllMembership) {
                if (mb.getMembershipDetail().getMembership() == m.getId()){
                    showMap.put(mb, m);
                    break;
                }
            }
        }

        float totalPageFloat = (float) listAllBuyHisByUsername.size() / pageNum;
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
        model.addAttribute("buyNum", listAllBuyHisByUsername.size());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageSelect", page);
        model.addAttribute("list", showMap);
        return new ModelAndView("userbuyhis");
    }

    @RequestMapping("user-buy-history/page-select/page={page}")
    public ModelAndView selectPageUserBuyHis(HttpServletResponse httpServletResponse,
                                              @PathVariable("page") int page) throws IOException {
        httpServletResponse.sendRedirect("/user-buy-history/page=" + page);
        return null;
    }
}
