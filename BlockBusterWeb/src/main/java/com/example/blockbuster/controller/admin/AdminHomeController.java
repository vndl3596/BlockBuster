package com.example.blockbuster.controller.admin;

import com.example.blockbuster.apicall.DataCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MovieDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@RestController
@RequestMapping("/admin/")
public class AdminHomeController {
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("home")
    public ModelAndView adminHomeShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();
        ArrayList<MovieDTO> listAllMovie;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/home");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        DataCall dataCall = new DataCall();
        dataCall.dataCall(session);
        listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");

        int viewNum = 0;
        for (MovieDTO mv : listAllMovie) {
            viewNum += mv.getViewNumber();
        }

        String urlGetAllUser = "http://localhost:8080/api/acc/getAllAccount";
        ResponseEntity<AccountDTO[]> responseAllAcc = restTemplate.getForEntity(urlGetAllUser, AccountDTO[].class);

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("listAllMovie", listAllMovie);
        model.addAttribute("viewNum", viewNum);
        model.addAttribute("accNum", responseAllAcc.getBody().length);
        return new ModelAndView("admin/index");
    }
}
