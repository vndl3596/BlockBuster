package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.util.MessageUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/")
public class ContactUsController {
    LoginResponse loginResponse;

    @RequestMapping(method = RequestMethod.GET, path = "contact-us")
    public ModelAndView contactShow(HttpSession session, Model model) {

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");

            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/contact-us");
        model.addAttribute("loginResponse", loginResponse);
        return new ModelAndView("contactus");
    }

    @RequestMapping(method = RequestMethod.POST, path = "contact-us")
    public ModelAndView contact(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "mail", required = true) String mail,
            @RequestParam(value = "contact-content", required = true) String content,
            HttpSession httpSession,
            Model model
    ) {

        loginResponse = (LoginResponse) httpSession.getAttribute("loginResponse");
        if (loginResponse != null) {
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) httpSession.getAttribute("loginAcc");
            model.addAttribute("loginAcc", loginAcc);
        }
        String error = MessageUtil.VALIDATION_CONTACT_SUCCESS;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("name", name);
        map.add("email", mail);
        map.add("content", content);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        String urlContact = "http://localhost:8080/api/contact";
        restTemplate.postForEntity(urlContact, request, Void.class);

        model.addAttribute("error", error);
        model.addAttribute("loginResponse", loginResponse);
        return new ModelAndView("contactus");
    }
}
