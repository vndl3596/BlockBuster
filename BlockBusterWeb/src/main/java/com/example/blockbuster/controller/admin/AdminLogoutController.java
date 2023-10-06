package com.example.blockbuster.controller.admin;

import com.example.blockbuster.dto.LoginResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/admin/")
public class AdminLogoutController {
    @RequestMapping(method = RequestMethod.GET, path = "logout")
    public ModelAndView login(
            Model model,
            HttpServletResponse res,
            HttpSession session
    ) throws IOException {
        LoginResponse loginResponse;
        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse != null) {
            RestTemplate restTemplate = new RestTemplate();
            String urlLogout = "http://localhost:8080/api/auth/logout/" + loginResponse.getUsername();
            restTemplate.getForEntity(urlLogout, String.class);

            session.removeAttribute("loginAdminResponse");
            session.removeAttribute("loginAdminAcc");
        }

        res.sendRedirect("/admin/login");
        return null;
    }
}
