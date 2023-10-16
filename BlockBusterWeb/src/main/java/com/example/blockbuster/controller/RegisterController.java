package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.RegisterRequest;
import com.example.blockbuster.dto.address.CityDTO;
import com.example.blockbuster.dto.address.TownDTO;
import com.example.blockbuster.util.MessageUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/")
public class RegisterController {
    RegisterRequest signUpAcc = new RegisterRequest();
    RestTemplate restTemplate = new RestTemplate();
    ArrayList<CityDTO> listCity;

    @RequestMapping(method = RequestMethod.GET, path = "register")
    public ModelAndView registerShow(Model model) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (listCity == null) {
            listCity = new ArrayList<>();
            String urlCity = "http://localhost:8080/api/address/get-all-city";
            ResponseEntity<CityDTO[]> response = restTemplate.getForEntity(urlCity, CityDTO[].class);
            Collections.addAll(listCity, response.getBody());
        }

        model.addAttribute("format", format);
        model.addAttribute("listCity", listCity);
        model.addAttribute("signUpAcc", signUpAcc);
        return new ModelAndView("register");
    }

    @RequestMapping(method = RequestMethod.POST, path = "register")
    public ModelAndView register(@RequestParam(value = "first-name", required = true) String firstname,
                                 @RequestParam(value = "last-name", required = true) String lastname,
                                 @RequestParam(value = "birth", required = true) String birth,
                                 @RequestParam(value = "gender", required = true) String gender,
                                 @RequestParam(value = "town", required = true) String town,
                                 @RequestParam(value = "email", required = true) String email,
                                 @RequestParam(value = "username", required = true) String username,
                                 @RequestParam(value = "password", required = true) String password,
                                 Model model,
                                 HttpSession session,
                                 HttpServletResponse res) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();
        boolean validation = true;

        String urlTown = "http://localhost:8080/api/address/getTownById/" + town;
        ResponseEntity<TownDTO> responseTown = restTemplate.getForEntity(urlTown, TownDTO.class);

        signUpAcc = new RegisterRequest(username, password, email, "", firstname, lastname, responseTown.getBody(), "", new SimpleDateFormat("yyyy-MM-dd").parse(birth), false, true);

        if(lastname == ""){
            model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_ERR03);
            validation = false;
        }
        if(firstname == ""){
            model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_ERR04);
            validation = false;
        }
        if(birth == ""){
            model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_ERR05);
            validation = false;
        }
        if(town == ""){
            model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_ERR06);
            validation = false;
        }
        if(email == ""){
            model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_ERR07);
            validation = false;
        }
        if(username == ""){
            model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_ERR08);
            validation = false;
        }
        if(password == ""){
            model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_ERR09);
            validation = false;
        }

        if(validation == false){
            model.addAttribute("format", format);
            model.addAttribute("signUpAcc", signUpAcc);
            model.addAttribute("listCity", listCity);
            return new ModelAndView("register");
        }

        if (gender.equals("true")) {
            signUpAcc.setGender(true);
        } else signUpAcc.setGender(false);
        String urlSignUp = "http://localhost:8080/api/auth/signup";

        HttpEntity<RegisterRequest> requestBody = new HttpEntity<>(signUpAcc);
        ResponseEntity<AccountDTO> responseSignUp = restTemplate.postForEntity(urlSignUp, requestBody, AccountDTO.class);

        AccountDTO reponse = new AccountDTO();
        if (responseSignUp.getStatusCode() == HttpStatus.OK) {
            reponse = responseSignUp.getBody();
        }
        if (reponse.getId() > 0) {
            model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_SUCCESS);
        } else {
            String urlCheckUsername = "http://localhost:8080/api/acc/getAccoutByUsername/" + username;
            ResponseEntity<AccountDTO> responseCheckUsername = restTemplate.getForEntity(urlCheckUsername, AccountDTO.class);
            if (responseCheckUsername.getBody().getId() > 0) {
                model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_ERR01);
            } else model.addAttribute("error", MessageUtil.VALIDATION_REGISTER_ERR02);
        }

        if (listCity == null) {
            listCity = new ArrayList<>();
            String urlCity = "http://localhost:8080/api/address/get-all-city";
            ResponseEntity<CityDTO[]> response = restTemplate.getForEntity(urlCity, CityDTO[].class);
            Collections.addAll(listCity, response.getBody());
        }

        model.addAttribute("format", format);
        model.addAttribute("signUpAcc", signUpAcc);
        model.addAttribute("listCity", listCity);
        return new ModelAndView("register");
    }
}
