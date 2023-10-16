package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.ImageDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.address.CityDTO;
import com.example.blockbuster.dto.address.DistrictDTO;
import com.example.blockbuster.dto.address.TownDTO;
import com.example.blockbuster.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/")
public class UserProfileController {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    AccountDTO acc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping(method = RequestMethod.GET, path = "user-profile")
    public ModelAndView userProfileShow(Model model, HttpSession session, HttpServletResponse response) throws IOException {

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/user-profile");
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");
        acc = loginAcc;
        RestTemplate restTemplate = new RestTemplate();
        String urlTown = "http://localhost:8080/api/address/getTownById/" + acc.getTown();
        ResponseEntity<TownDTO> responseTown = restTemplate.getForEntity(urlTown, TownDTO.class);

        String urlListTownByDistrict = "http://localhost:8080/api/address/get-town/" + responseTown.getBody().getDistrictId();
        ResponseEntity<TownDTO[]> responseTownByDistrict = restTemplate.getForEntity(urlListTownByDistrict, TownDTO[].class);
        ArrayList<TownDTO> listTownByDistrict = new ArrayList<>();
        Collections.addAll(listTownByDistrict, responseTownByDistrict.getBody());

        String urlDistrict = "http://localhost:8080/api/address/getDistrictById/" + responseTown.getBody().getDistrictId();
        ResponseEntity<DistrictDTO> responseDistrict = restTemplate.getForEntity(urlDistrict, DistrictDTO.class);

        String urlListDistrictByCity = "http://localhost:8080/api/address/get-district/" + responseDistrict.getBody().getCityId();
        ResponseEntity<DistrictDTO[]> responseDistrictByCity = restTemplate.getForEntity(urlListDistrictByCity, DistrictDTO[].class);
        ArrayList<DistrictDTO> listDistrictByCity = new ArrayList<>();
        Collections.addAll(listDistrictByCity, responseDistrictByCity.getBody());

        String urlListCity = "http://localhost:8080/api/address/get-all-city";
        ResponseEntity<CityDTO[]> responseListCity = restTemplate.getForEntity(urlListCity, CityDTO[].class);
        ArrayList<CityDTO> listCity = new ArrayList<>();
        Collections.addAll(listCity, responseListCity.getBody());

        model.addAttribute("acc", acc);
        model.addAttribute("accTown", responseTown.getBody().getId());
        model.addAttribute("accDistrict", responseDistrict.getBody().getId());
        model.addAttribute("accCity", responseDistrict.getBody().getCityId());
        model.addAttribute("listTownByDistrict", listTownByDistrict);
        model.addAttribute("listDistrictByCity", listDistrictByCity);
        model.addAttribute("listCity", listCity);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("format", format);
        return new ModelAndView("userprofile");
    }

    @RequestMapping(method = RequestMethod.POST, path = "user-profile")
    public ModelAndView userProfile(
            @RequestParam(value = "first-name", required = true) String firstname,
            @RequestParam(value = "last-name", required = true) String lastname,
            @RequestParam(value = "birth", required = true) String birth,
            @RequestParam(value = "address", required = true) String address,
            @RequestParam(value = "town", required = true) int town,
            @RequestParam(value = "gender", required = true) Boolean gender,
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "email", required = true) String email,
            HttpSession session,
            HttpServletResponse response,
            Model model
    ) throws ParseException, IOException {

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/user-profile");
            response.sendRedirect("/login");
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();
        AccountDTO oldAcc = new AccountDTO(acc.getId(), acc.getUsername(), acc.getPassword(), acc.isEnabled(), acc.getEmail(), acc.getAvatar(), acc.getFirstname(), acc.getLastname(), acc.getBirthday(), acc.getTown(), acc.getAddress(), acc.getPhoneNumber(), acc.isGender(), acc.getRoles());
        acc.setLastname(lastname);
        acc.setFirstname(firstname);
        acc.setBirthday(format.parse(birth));
        acc.setAddress(address);
        acc.setGender(gender);
        acc.setEmail(email);
        acc.setPhoneNumber(phone);
        acc.setTown(town);
        acc.setAvatar("false");
        HttpEntity<AccountDTO> requestBody = new HttpEntity<>(acc);
        String urlChangeInfo = "http://localhost:8080/api/acc/edit";
        ResponseEntity<AccountDTO> responseChangeInfo = restTemplate.exchange(urlChangeInfo, HttpMethod.PUT, requestBody, AccountDTO.class);
        if (responseChangeInfo.getBody().getId() == 0) {
            model.addAttribute("errorCI", MessageUtil.VALIDATION_USERPROFILE_ERR01);
            acc = oldAcc;
        } else {
            acc.setAvatar(oldAcc.getAvatar());
            model.addAttribute("errorCI", MessageUtil.VALIDATION_USERPROFILE_SUCCESS);
        }
        String urlTown = "http://localhost:8080/api/address/getTownById/" + acc.getTown();
        ResponseEntity<TownDTO> responseTown = restTemplate.getForEntity(urlTown, TownDTO.class);

        String urlListTownByDistrict = "http://localhost:8080/api/address/get-town/" + responseTown.getBody().getDistrictId();
        ResponseEntity<TownDTO[]> responseTownByDistrict = restTemplate.getForEntity(urlListTownByDistrict, TownDTO[].class);
        ArrayList<TownDTO> listTownByDistrict = new ArrayList<>();
        Collections.addAll(listTownByDistrict, responseTownByDistrict.getBody());

        String urlDistrict = "http://localhost:8080/api/address/getDistrictById/" + responseTown.getBody().getDistrictId();
        ResponseEntity<DistrictDTO> responseDistrict = restTemplate.getForEntity(urlDistrict, DistrictDTO.class);

        String urlListDistrictByCity = "http://localhost:8080/api/address/get-district/" + responseDistrict.getBody().getCityId();
        ResponseEntity<DistrictDTO[]> responseDistrictByCity = restTemplate.getForEntity(urlListDistrictByCity, DistrictDTO[].class);
        ArrayList<DistrictDTO> listDistrictByCity = new ArrayList<>();
        Collections.addAll(listDistrictByCity, responseDistrictByCity.getBody());

        String urlListCity = "http://localhost:8080/api/address/get-all-city";
        ResponseEntity<CityDTO[]> responseListCity = restTemplate.getForEntity(urlListCity, CityDTO[].class);
        ArrayList<CityDTO> listCity = new ArrayList<>();
        Collections.addAll(listCity, responseListCity.getBody());

        session.setAttribute("loginAcc", acc);
        model.addAttribute("acc", acc);
        model.addAttribute("accTown", responseTown.getBody().getId());
        model.addAttribute("accDistrict", responseDistrict.getBody().getId());
        model.addAttribute("accCity", responseDistrict.getBody().getCityId());
        model.addAttribute("listTownByDistrict", listTownByDistrict);
        model.addAttribute("listDistrictByCity", listDistrictByCity);
        model.addAttribute("listCity", listCity);
        model.addAttribute("format", format);
        return new ModelAndView("userprofile");
    }

    @RequestMapping(method = RequestMethod.GET, path = "change-pass")
    public ModelAndView userPassChangeGet(
            HttpServletResponse response) throws IOException {
        response.sendRedirect("/user-profile");
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, path = "change-pass")
    public ModelAndView userPassChangePost(
            @RequestParam(value = "oldpassword", required = true) String oldPassword,
            @RequestParam(value = "newpassword", required = true) String newPassword,
            @RequestParam(value = "renewpassword", required = true) String renewPassword,
            Model model,
            HttpServletResponse response,
            HttpSession session
    ) throws IOException {

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/user-profile");
            response.sendRedirect("/login");
            return null;
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Password> requestBodyCheck = new HttpEntity<>(new Password(oldPassword));
        String urlCheckPass = "http://localhost:8080/api/acc/checkPassword/" + acc.getUsername();
        ResponseEntity<String> responseCheckPass = restTemplate.postForEntity(urlCheckPass, requestBodyCheck, String.class);
        if (responseCheckPass.getBody().equals("true")) {
            HttpEntity<Password> requestBodyChange = new HttpEntity<>(new Password(newPassword));
            String urlChangePass = "http://localhost:8080/api/acc/changePassword/" + acc.getUsername();
            ResponseEntity<String> responseChangePass = restTemplate.postForEntity(urlChangePass, requestBodyChange, String.class);
            if (responseChangePass.getBody().equals("true")) {
                model.addAttribute("errorP", MessageUtil.VALIDATION_CHANGEPASS_SUCCESS);
            } else model.addAttribute("errorP", MessageUtil.VALIDATION_CHANGEPASS_ERR01);
        } else model.addAttribute("errorP", MessageUtil.VALIDATION_CHANGEPASS_ERR02);

        String urlTown = "http://localhost:8080/api/address/getTownById/" + acc.getTown();
        ResponseEntity<TownDTO> responseTown = restTemplate.getForEntity(urlTown, TownDTO.class);

        String urlListTownByDistrict = "http://localhost:8080/api/address/get-town/" + responseTown.getBody().getDistrictId();
        ResponseEntity<TownDTO[]> responseTownByDistrict = restTemplate.getForEntity(urlListTownByDistrict, TownDTO[].class);
        ArrayList<TownDTO> listTownByDistrict = new ArrayList<>();
        Collections.addAll(listTownByDistrict, responseTownByDistrict.getBody());

        String urlDistrict = "http://localhost:8080/api/address/getDistrictById/" + responseTown.getBody().getDistrictId();
        ResponseEntity<DistrictDTO> responseDistrict = restTemplate.getForEntity(urlDistrict, DistrictDTO.class);

        String urlListDistrictByCity = "http://localhost:8080/api/address/get-district/" + responseDistrict.getBody().getCityId();
        ResponseEntity<DistrictDTO[]> responseDistrictByCity = restTemplate.getForEntity(urlListDistrictByCity, DistrictDTO[].class);
        ArrayList<DistrictDTO> listDistrictByCity = new ArrayList<>();
        Collections.addAll(listDistrictByCity, responseDistrictByCity.getBody());

        String urlListCity = "http://localhost:8080/api/address/get-all-city";
        ResponseEntity<CityDTO[]> responseListCity = restTemplate.getForEntity(urlListCity, CityDTO[].class);
        ArrayList<CityDTO> listCity = new ArrayList<>();
        Collections.addAll(listCity, responseListCity.getBody());

        model.addAttribute("acc", acc);
        model.addAttribute("accTown", responseTown.getBody().getId());
        model.addAttribute("accDistrict", responseDistrict.getBody().getId());
        model.addAttribute("accCity", responseDistrict.getBody().getCityId());
        model.addAttribute("listTownByDistrict", listTownByDistrict);
        model.addAttribute("listDistrictByCity", listDistrictByCity);
        model.addAttribute("listCity", listCity);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("format", format);
        return new ModelAndView("userprofile");
    }

    @RequestMapping(method = RequestMethod.GET, path = "change-avt")
    public ModelAndView avtChangeGet(
            HttpServletResponse response) throws IOException {
        response.sendRedirect("/user-profile");
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, path = "change-avt")
    public ModelAndView avtChange(
            @RequestParam(value = "avt", required = true) MultipartFile avt,
            Model model,
            HttpServletResponse response,
            HttpSession session
    ) throws IOException {

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/user-profile");
            response.sendRedirect("/login");
            return null;
        }
        String fs = avt.getOriginalFilename();
        String filenameL = fs.substring(fs.indexOf("."), fs.length());
        RestTemplate restTemplate = new RestTemplate();
        AccountDTO sendAcc;

        sendAcc = new AccountDTO(acc.getId(), acc.getUsername(), acc.getPassword(), acc.isEnabled(), acc.getEmail(), "./image/account/user-" + acc.getId() + filenameL, acc.getFirstname(), acc.getLastname(), acc.getBirthday(), acc.getTown(), acc.getAddress(), acc.getPhoneNumber(), acc.isGender(), acc.getRoles());
        HttpEntity<AccountDTO> requestBody = new HttpEntity<>(sendAcc);
        String urlChangeImageForEmpty = "http://localhost:8080/api/acc/edit";
        ResponseEntity<AccountDTO> responseChangeImageForEmpty = restTemplate.exchange(urlChangeImageForEmpty, HttpMethod.PUT, requestBody, AccountDTO.class);

        HttpHeaders headersUploadImage = new HttpHeaders();
        headersUploadImage.setContentType(MediaType.MULTIPART_FORM_DATA);
        Resource muti = avt.getResource();
        MultiValueMap<String, Object> mapUp = new LinkedMultiValueMap<String, Object>();
        mapUp.add("image", muti);
        HttpEntity<MultiValueMap<String, Object>> requestUp = new HttpEntity<>(mapUp, headersUploadImage);
        String urlUpdateImage = "http://localhost:8080/uploadimage/" + "user-" + sendAcc.getId() + "/121";
        ResponseEntity<JSONObject> responseUpdateImage = restTemplate.postForEntity(urlUpdateImage, requestUp, JSONObject.class);

        HttpHeaders headersGetImage = new HttpHeaders();
        headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> mapGet = new LinkedMultiValueMap<String, String>();
        mapGet.add("url", sendAcc.getAvatar());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(mapGet, headersGetImage);
        String urlImage = "http://localhost:8080/getImage";
        ResponseEntity<ImageDTO> responseGetImage = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
        acc.setAvatar(responseGetImage.getBody().getUrl());

        session.setAttribute("loginAcc", acc);
        response.sendRedirect("/user-profile");
        return null;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public class Password {
        String password;
    }
}
