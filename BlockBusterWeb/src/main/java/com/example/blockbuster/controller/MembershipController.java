package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MembershipDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/")
public class MembershipController {

    LoginResponse loginResponse;

    @RequestMapping("membership")
    public ModelAndView viewMembership(Model model,
                                       HttpSession session,
                                       HttpServletResponse response) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/membership");
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("listAllMembership", listAllMembership);

        return new ModelAndView("membership");
    }

    @RequestMapping(method = RequestMethod.GET, path = "membership/buy")
    public ModelAndView buyMembershipGet(Model model,
                                       HttpSession session,
                                       HttpServletResponse response) throws IOException {
        response.sendRedirect("/membership");
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, path = "membership/buy")
    public ModelAndView buyMembershipPost(Model model,
                                          HttpSession session,
                                          HttpServletResponse response,
                                          @RequestParam(value = "inputMemberDetailId", required = true) String buyMemberDetailId) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/membership");
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");

        String urlBuyMembership = "http://localhost:8080/api/membership-detail/buy/" + loginAcc.getId() + "&" + buyMemberDetailId;
        ResponseEntity<String> responseBuyMembership = restTemplate.getForEntity(urlBuyMembership, String.class);

        String urlBuyMembershipHis = "http://localhost:8080/api/membership-buy-his/save/" + loginAcc.getId() + "&" + buyMemberDetailId;
        ResponseEntity<String> responseBuyMembershipHis = restTemplate.getForEntity(urlBuyMembershipHis, String.class);

        response.sendRedirect("/membership");
        return null;
    }
}