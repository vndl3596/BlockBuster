package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.ImageDTO;
import com.example.blockbuster.dto.LoginAcc;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.util.MessageUtil;
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

    LoginAcc acc = new LoginAcc();

    @RequestMapping(method = RequestMethod.GET, path = "login")
    public ModelAndView loginShow(Model model) {
        model.addAttribute("acc", acc);
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
        acc.setUsername(username);
        acc.setPassword(password);
        String urlLogin = "http://localhost:8080/api/auth/login";

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
                if (loginAcc.getAvatar().equals("") == false) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                    map.add("url", loginAcc.getAvatar());
                    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                    String urlImage = "http://localhost:8080/getImage";
                    ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                    loginAcc.setAvatar(response.getBody().getUrl());
                }
                session.setAttribute("loginResponse", reponse);
                session.setAttribute("loginAcc", loginAcc);
                if (session.getAttribute("oldUrl") != null) {
                    res.sendRedirect((String) session.getAttribute("oldUrl"));
                } else {
                    res.sendRedirect("/admin");
                }
            } else if (loginAcc.isEnabled() == false) model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR01);
        } else {
            String urlCheckUsername = "http://localhost:8080/api/acc/getAccoutByUsername/" + username;
            ResponseEntity<AccountDTO> responseCheckUsername = restTemplate.getForEntity(urlCheckUsername, AccountDTO.class);

            if ((responseCheckUsername.getBody().getId() > 0) && (!responseCheckUsername.getBody().isEnabled())) {
                model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR01);
            } else model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR02);
        }
        model.addAttribute("acc", acc);
        return new ModelAndView("login");
    }
}
