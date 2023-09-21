package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.LoginResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/")
public class TermUsesController {
    LoginResponse loginResponse;

    @RequestMapping("termuses")
    public ModelAndView termUsesShow(HttpSession session, Model model) {
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }

        model.addAttribute("loginResponse", loginResponse);
        session.setAttribute("oldUrl", "/termuses");
        return new ModelAndView("termuse");
    }
}
