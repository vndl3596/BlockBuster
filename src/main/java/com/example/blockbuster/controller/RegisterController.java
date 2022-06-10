package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.LoginAcc;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.RegisterRequest;
import com.example.blockbuster.dto.address.CityDTO;
import com.example.blockbuster.dto.address.TownDTO;
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
import java.util.Date;

@RestController
@RequestMapping("/")
public class RegisterController {
    @RequestMapping(method = RequestMethod.GET, path = "register")
    public ModelAndView registerShow(Model model) {
        ArrayList<CityDTO> listCity = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String urlCity = "http://localhost:8080/api/address/get-all-city";
        ResponseEntity<CityDTO[]> response = restTemplate.getForEntity(urlCity, CityDTO[].class);
        Collections.addAll(listCity, response.getBody());
        model.addAttribute("listCity",listCity);
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
                                 @RequestParam(value = "repassword", required = true) String repassword,
                                 Model model,
                                 HttpSession session,
                                 HttpServletResponse res) throws ParseException, IOException {
        //thêm API đăng ký vào đây
        RestTemplate restTemplate = new RestTemplate();
        String urlTown = "http://localhost:8080/api/address/getAddressByTownId/" + town;
        ResponseEntity<TownDTO> responseTown = restTemplate.getForEntity(urlTown, TownDTO.class);

        RegisterRequest signUpAcc = new RegisterRequest(username, password, email,"",firstname,lastname,responseTown.getBody(),"",new SimpleDateFormat("yyyy-MM-dd").parse(birth),true,true);
        if(gender.equals("Male")){
            signUpAcc.setGender(true);
        }else signUpAcc.setGender(false);
        String urlSignUp = "http://localhost:8080/api/auth/signup";

        HttpEntity<RegisterRequest> requestBody = new HttpEntity<>(signUpAcc);
        ResponseEntity<AccountDTO> responseSignUp = restTemplate.postForEntity(urlSignUp, requestBody, AccountDTO.class);

        AccountDTO reponse = new AccountDTO();
        if(responseSignUp.getStatusCode() == HttpStatus.OK){
            reponse = responseSignUp.getBody();
        }
        if (reponse.getId() > 0){
            model.addAttribute("error", "Đăng ký thành công!!! Check Email để kích hoạt tài khoản!");
        } else {
            String urlCheckUsername = "http://localhost:8080/api/acc/getAccoutByUsername/" + username;
            ResponseEntity<AccountDTO> responseCheckUsername = restTemplate.getForEntity(urlCheckUsername, AccountDTO.class);
            if(responseCheckUsername.getBody().getId() > 0){
                model.addAttribute("error", "Đăng ký thất bại!!! Tài khoản đã có người sử dụng!");
            } else model.addAttribute("error", "Đăng ký thất bại!!! Email đã có người sử dụng!");
        }
        return new ModelAndView("register");
    }
}
