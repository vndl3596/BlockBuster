package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.LoginAcc;
import com.example.blockbuster.dto.LoginResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/")
public class LogoutController {
    @RequestMapping(method = RequestMethod.GET, path = "logout")
    public ModelAndView login(
            Model model,
            HttpServletResponse res,
            HttpSession session
    ) throws IOException {
        LoginResponse loginResponse;
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if(loginResponse != null){
            session.removeAttribute("loginResponse");
            session.removeAttribute("loginAcc");
            res.sendRedirect((String) session.getAttribute("oldUrl"));
        }
        else res.sendRedirect("/home");
        return null;
    }
}
