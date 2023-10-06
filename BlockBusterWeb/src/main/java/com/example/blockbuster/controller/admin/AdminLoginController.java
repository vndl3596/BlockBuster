package com.example.blockbuster.controller.admin;

import com.example.blockbuster.dto.*;
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
@RequestMapping("/admin/")
public class AdminLoginController {
    LoginAcc acc = new LoginAcc();

    @RequestMapping(method = RequestMethod.GET, path = "login")
    public ModelAndView adminLoginShow(Model model) {
        model.addAttribute("acc", acc);
        return new ModelAndView("admin/login");
    }

    @RequestMapping(method = RequestMethod.POST, path = "login")
    public ModelAndView login(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            Model model,
            HttpServletResponse res,
            HttpSession session
    ) throws IOException {
        if(username == ""){
            model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR05);
            model.addAttribute("acc", acc);
            return new ModelAndView("login");
        }
        if(password == ""){
            model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR06);
            model.addAttribute("acc", acc);
            return new ModelAndView("login");
        }

        acc.setUsername(username);
        acc.setPassword(password);

        RestTemplate restTemplate = new RestTemplate();
        String urlCheckIsLogin = "http://localhost:8080/api/auth/isLogin/" + username;
        if(restTemplate.getForEntity(urlCheckIsLogin, Integer.class).getBody() == 1){
            model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR04);
            model.addAttribute("acc", acc);
            return new ModelAndView("admin/login");
        }

        String urlLogin = "http://localhost:8080/api/auth/login";
        HttpEntity<LoginAcc> requestBody = new HttpEntity<>(acc);
        ResponseEntity<LoginResponse> responseLogin = restTemplate.postForEntity(urlLogin, requestBody, LoginResponse.class);

        LoginResponse reponse = new LoginResponse();
        boolean roleCheck = false;
        if (responseLogin.getStatusCode() == HttpStatus.OK) {
            reponse = responseLogin.getBody();
        }
        if (reponse.getAccId() > 0) {
            for (AccountRoleDTO role : reponse.getAccountRoleDTO()) {
                if (role.getId() == 1) {
                    roleCheck = true;
                }
            }
        }

        if ((reponse.getAccId() > 0) && (roleCheck == true)) {
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
                session.setAttribute("loginAdminResponse", reponse);
                session.setAttribute("loginAdminAcc", loginAcc);
                if (session.getAttribute("oldAdminUrl") != null) {
                    res.sendRedirect((String) session.getAttribute("oldAdminUrl"));
                } else {
                    res.sendRedirect("/admin/home");
                }
            } else if (loginAcc.isEnabled() == false) model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR01);
        } else if ((reponse.getAccId() > 0) && (roleCheck == false)) {
            model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR03);
        } else {
            String urlCheckUsername = "http://localhost:8080/api/acc/getAccoutByUsername/" + username;
            ResponseEntity<AccountDTO> responseCheckUsername = restTemplate.getForEntity(urlCheckUsername, AccountDTO.class);

            if ((responseCheckUsername.getBody().getId() > 0) && (!responseCheckUsername.getBody().isEnabled())) {
                model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR01);
            } else model.addAttribute("error", MessageUtil.VALIDATION_LOGIN_ERR02);
        }
        model.addAttribute("acc", acc);
        return new ModelAndView("admin/login");
    }
}
