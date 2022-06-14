package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.ImageDTO;
import com.example.blockbuster.dto.LoginAcc;
import com.example.blockbuster.dto.LoginResponse;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
public class LoginController {

    @RequestMapping(method = RequestMethod.GET, path = "login")
    public ModelAndView loginShow() {
        return new ModelAndView("login");
    }

    @RequestMapping(method = RequestMethod.POST, path = "login")
    public ModelAndView login(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            Model model,
            HttpServletResponse res,
            HttpSession session
    ) throws IOException {
        String urlLogin = "http://localhost:8080/api/auth/login";
        LoginAcc acc = new LoginAcc(username, password);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<LoginAcc> requestBody = new HttpEntity<>(acc);
        ResponseEntity<LoginResponse> responseLogin = restTemplate.postForEntity(urlLogin, requestBody, LoginResponse.class);

        LoginResponse reponse = new LoginResponse();
        if (responseLogin.getStatusCode() == HttpStatus.OK) {
            reponse = responseLogin.getBody();
        }
        if (reponse.getAccId() > 0) {
            AccountDTO loginAcc = new AccountDTO();
            String urlAccount = "http://localhost:8080/api/acc/getAccById/" + reponse.getAccId();
            ResponseEntity<AccountDTO> responseAccount = restTemplate.getForEntity(urlAccount, AccountDTO.class);
            loginAcc = responseAccount.getBody();
            if (loginAcc.getId() > 0) {
                session.setAttribute("loginResponse", reponse);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", loginAcc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                loginAcc.setAvatar(response.getBody().getUrl());
                session.setAttribute("loginAcc", loginAcc);
                res.sendRedirect((String) session.getAttribute("oldUrl"));
            } else model.addAttribute("error", "Tài khoản chưa được kích hoạt!");
        } else {
            String urlCheckUsername = "http://localhost:8080/api/acc/getAccoutByUsername/" + username;
            ResponseEntity<AccountDTO> responseCheckUsername = restTemplate.getForEntity(urlCheckUsername, AccountDTO.class);

            if ((responseCheckUsername.getBody().getId() > 0) && (!responseCheckUsername.getBody().isEnabled())) {
                model.addAttribute("error", "Tài khoản chưa được kích hoạt!");
            } else model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
        }
        return new ModelAndView("login");
    }
}
