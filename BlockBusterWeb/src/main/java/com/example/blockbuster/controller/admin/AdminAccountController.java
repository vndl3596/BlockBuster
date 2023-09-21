package com.example.blockbuster.controller.admin;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.ImageDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.address.CityDTO;
import com.example.blockbuster.dto.address.DistrictDTO;
import com.example.blockbuster.dto.address.TownDTO;
import org.json.simple.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/admin/")
public class AdminAccountController {
    ArrayList<AccountDTO> listAllAccount;
    AccountDTO addAccount;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("/account")
    public ModelAndView adminAccountShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/account");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllAccount == null) {
            listAllAccount = new ArrayList<>();
            String urlGetAllAccount = "http://localhost:8080/api/acc/getAllAccount";
            ResponseEntity<AccountDTO[]> reponseGetAllAccount = restTemplate.getForEntity(urlGetAllAccount, AccountDTO[].class);
            Collections.addAll(listAllAccount, reponseGetAllAccount.getBody());
            Collections.sort(listAllAccount, new Comparator<AccountDTO>() {
                @Override
                public int compare(AccountDTO o1, AccountDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
            for (AccountDTO acc : listAllAccount) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", acc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                acc.setAvatar(response.getBody().getUrl());
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("listAllAccount", listAllAccount);
        return new ModelAndView("admin/user");
    }

    @RequestMapping("account/view/idAcc={id}")
    public ModelAndView adminAccountView(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/account/view/idAcc=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllAccount == null) {
            listAllAccount = new ArrayList<>();
            String urlGetAllAccount = "http://localhost:8080/api/acc/getAllAccount";
            ResponseEntity<AccountDTO[]> reponseGetAllAccount = restTemplate.getForEntity(urlGetAllAccount, AccountDTO[].class);
            Collections.addAll(listAllAccount, reponseGetAllAccount.getBody());
            Collections.sort(listAllAccount, new Comparator<AccountDTO>() {
                @Override
                public int compare(AccountDTO o1, AccountDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
            for (AccountDTO acc : listAllAccount) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", acc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                acc.setAvatar(response.getBody().getUrl());
            }
        }

        AccountDTO viewAcc = new AccountDTO();
        for (AccountDTO acc : listAllAccount) {
            if (acc.getId() == id) {
                viewAcc = acc;
            }
        }

        String urlTown = "http://localhost:8080/api/address/getTownById/" + viewAcc.getTown();
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

        Map<String, Integer> mapRolesOnAccount = new TreeMap<>();
        mapRolesOnAccount.put("User", 0);
        mapRolesOnAccount.put("Admin", 0);
        boolean loop = false;
        for (String roles : viewAcc.getRoles()) {
            loop = false;
            if (roles.equals("user")) {
                mapRolesOnAccount.put("User", 1);
            }
            if (roles.equals("admin")) {
                mapRolesOnAccount.put("Admin", 1);
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("mapRolesOnAccount", mapRolesOnAccount);
        model.addAttribute("accTown", responseTown.getBody().getId());
        model.addAttribute("accDistrict", responseDistrict.getBody().getId());
        model.addAttribute("accCity", responseDistrict.getBody().getCityId());
        model.addAttribute("listTownByDistrict", listTownByDistrict);
        model.addAttribute("listDistrictByCity", listDistrictByCity);
        model.addAttribute("listCity", listCity);
        model.addAttribute("viewAcc", viewAcc);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        return new ModelAndView("admin/viewuser");
    }

    @RequestMapping("account/add")
    public ModelAndView adminAccountAddGet(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();
        addAccount = new AccountDTO();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/account/add");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllAccount == null) {
            listAllAccount = new ArrayList<>();
            String urlGetAllAccount = "http://localhost:8080/api/acc/getAllAccount";
            ResponseEntity<AccountDTO[]> reponseGetAllAccount = restTemplate.getForEntity(urlGetAllAccount, AccountDTO[].class);
            Collections.addAll(listAllAccount, reponseGetAllAccount.getBody());
            Collections.sort(listAllAccount, new Comparator<AccountDTO>() {
                @Override
                public int compare(AccountDTO o1, AccountDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
            for (AccountDTO acc : listAllAccount) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", acc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                acc.setAvatar(response.getBody().getUrl());
            }
        }

        String urlListCity = "http://localhost:8080/api/address/get-all-city";
        ResponseEntity<CityDTO[]> responseListCity = restTemplate.getForEntity(urlListCity, CityDTO[].class);
        ArrayList<CityDTO> listCity = new ArrayList<>();
        Collections.addAll(listCity, responseListCity.getBody());

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", "");
        model.addAttribute("addAccount", addAccount);
        model.addAttribute("listCity", listCity);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        return new ModelAndView("admin/adduser");
    }

    @RequestMapping(method = RequestMethod.POST, path = "account/add")
    public ModelAndView adminAccountAddPost(Model model, HttpSession session,
                                            @RequestParam("avatar") MultipartFile avatar,
                                            @RequestParam("haveavatar") String haveavatar,
                                            @RequestParam("lastname") String lastname,
                                            @RequestParam("firstname") String firstname,
                                            @RequestParam("birthday") String birthday,
                                            @RequestParam("gender") String gender,
                                            @RequestParam("phone") String phone,
                                            @RequestParam("email") String email,
                                            @RequestParam("town") String town,
                                            @RequestParam("address") String address,
                                            @RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            @RequestParam("roles") String[] roles,
                                            HttpServletResponse servletResponse) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String error = "";
        Boolean g;
        if (gender.equals("1")) {
            g = true;
        } else g = false;
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/account/add");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        List<String> rolesOnAccount = new ArrayList<>();
        Collections.addAll(rolesOnAccount, roles);
        if (haveavatar.equals("false")) {
            addAccount = new AccountDTO(0, username, password, true, email, haveavatar, firstname, lastname, inputDateFormat.parse(birthday), Integer.parseInt(town), address, phone, g, rolesOnAccount);
        } else {
            String fs = avatar.getOriginalFilename();
            String filenameL = fs.substring(fs.indexOf("."), fs.length());
            addAccount = new AccountDTO(0, username, password, true, email, filenameL, firstname, lastname, inputDateFormat.parse(birthday), Integer.parseInt(town), address, phone, g, rolesOnAccount);
        }


        for (AccountDTO acc : listAllAccount) {
            if (acc.getUsername().equals(addAccount.getUsername())) {
                error += "Tài khoản đã được sử dụng!";
                break;
            }
            if (acc.getEmail().equals(addAccount.getEmail())) {
                error += "Email đã được sử dụng!";
                break;
            }
        }
        if (error.equals("")) {
            error += "Thêm tài khoản thành công!";
            String urlAddAccount = "http://localhost:8080/api/acc/createAcc";
            HttpEntity<AccountDTO> requestAddAccount = new HttpEntity<>(addAccount);
            ResponseEntity<AccountDTO> responseAddAccount = restTemplate.postForEntity(urlAddAccount, requestAddAccount, AccountDTO.class);
            listAllAccount.add(0, responseAddAccount.getBody());
            if (haveavatar.equals("false")) {
                HttpHeaders headersGetImage = new HttpHeaders();
                headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", listAllAccount.get(0).getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersGetImage);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                listAllAccount.get(0).setAvatar(response.getBody().getUrl());
            } else {
                HttpHeaders headersUploadImage = new HttpHeaders();
                headersUploadImage.setContentType(MediaType.MULTIPART_FORM_DATA);
                Resource muti = avatar.getResource();
                MultiValueMap<String, Object> mapUp = new LinkedMultiValueMap<String, Object>();
                mapUp.add("image", muti);
                HttpEntity<MultiValueMap<String, Object>> requestUp = new HttpEntity<>(mapUp, headersUploadImage);
                String urlUpdateImage = "http://localhost:8080/uploadimage/" + "user-" + responseAddAccount.getBody().getId() + "/121";
                restTemplate.postForEntity(urlUpdateImage, requestUp, JSONObject.class);

                HttpHeaders headersGetImage = new HttpHeaders();
                headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", listAllAccount.get(0).getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersGetImage);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                listAllAccount.get(0).setAvatar(response.getBody().getUrl());
            }
        }
        if (listAllAccount == null) {
            listAllAccount = new ArrayList<>();
            String urlGetAllAccount = "http://localhost:8080/api/acc/getAllAccount";
            ResponseEntity<AccountDTO[]> reponseGetAllAccount = restTemplate.getForEntity(urlGetAllAccount, AccountDTO[].class);
            Collections.addAll(listAllAccount, reponseGetAllAccount.getBody());
            Collections.sort(listAllAccount, new Comparator<AccountDTO>() {
                @Override
                public int compare(AccountDTO o1, AccountDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
            for (AccountDTO acc : listAllAccount) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", acc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                acc.setAvatar(response.getBody().getUrl());
            }
        }

        String urlListCity = "http://localhost:8080/api/address/get-all-city";
        ResponseEntity<CityDTO[]> responseListCity = restTemplate.getForEntity(urlListCity, CityDTO[].class);
        ArrayList<CityDTO> listCity = new ArrayList<>();
        Collections.addAll(listCity, responseListCity.getBody());

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", error);
        model.addAttribute("addAccount", addAccount);
        model.addAttribute("listCity", listCity);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        return new ModelAndView("admin/adduser");
    }

    @RequestMapping("account/delete/idAcc={id}")
    public ModelAndView adminAccountDelete(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String error = "";
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/account");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllAccount == null) {
            listAllAccount = new ArrayList<>();
            String urlGetAllAccount = "http://localhost:8080/api/acc/getAllAccount";
            ResponseEntity<AccountDTO[]> reponseGetAllAccount = restTemplate.getForEntity(urlGetAllAccount, AccountDTO[].class);
            Collections.addAll(listAllAccount, reponseGetAllAccount.getBody());
            Collections.sort(listAllAccount, new Comparator<AccountDTO>() {
                @Override
                public int compare(AccountDTO o1, AccountDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
            for (AccountDTO acc : listAllAccount) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", acc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                acc.setAvatar(response.getBody().getUrl());
            }
        }

        for (AccountDTO acc : listAllAccount) {
            if (acc.getId() == id) {
                String urlDeleteAcc = "http://localhost:8080/api/acc/deleteAcc/" + acc.getUsername();
                restTemplate.getForEntity(urlDeleteAcc, String.class);
                listAllAccount.remove(acc);
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("listAllAccount", listAllAccount);
        return new ModelAndView("admin/user");
    }

    @RequestMapping("account/edit/idAcc={id}")
    public ModelAndView adminAccountEditGet(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();
        String error = "";

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/account/edit/idAcc=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllAccount == null) {
            listAllAccount = new ArrayList<>();
            String urlGetAllAccount = "http://localhost:8080/api/acc/getAllAccount";
            ResponseEntity<AccountDTO[]> reponseGetAllAccount = restTemplate.getForEntity(urlGetAllAccount, AccountDTO[].class);
            Collections.addAll(listAllAccount, reponseGetAllAccount.getBody());
            Collections.sort(listAllAccount, new Comparator<AccountDTO>() {
                @Override
                public int compare(AccountDTO o1, AccountDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
            for (AccountDTO acc : listAllAccount) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", acc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                acc.setAvatar(response.getBody().getUrl());
            }
        }

        AccountDTO editAcc = new AccountDTO();
        for (AccountDTO acc : listAllAccount) {
            if (acc.getId() == id) {
                editAcc = acc;
            }
        }

        String urlTown = "http://localhost:8080/api/address/getTownById/" + editAcc.getTown();
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

        Map<String, Integer> mapRolesOnAccount = new TreeMap<>();
        mapRolesOnAccount.put("User", 0);
        mapRolesOnAccount.put("Admin", 0);
        boolean loop = false;
        for (String roles : editAcc.getRoles()) {
            loop = false;
            if (roles.equals("user")) {
                mapRolesOnAccount.put("User", 1);
            }
            if (roles.equals("admin")) {
                mapRolesOnAccount.put("Admin", 1);
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("mapRolesOnAccount", mapRolesOnAccount);
        model.addAttribute("accTown", responseTown.getBody().getId());
        model.addAttribute("accDistrict", responseDistrict.getBody().getId());
        model.addAttribute("accCity", responseDistrict.getBody().getCityId());
        model.addAttribute("listTownByDistrict", listTownByDistrict);
        model.addAttribute("listDistrictByCity", listDistrictByCity);
        model.addAttribute("listCity", listCity);
        model.addAttribute("editAcc", editAcc);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        model.addAttribute("error", error);
        return new ModelAndView("admin/edituser");
    }

    @RequestMapping(method = RequestMethod.POST, path = "account/edit/idAcc={id}")
    public ModelAndView adminAccountEditPost(Model model, HttpSession session, @PathVariable("id") int id,
                                             @RequestParam("avatar") MultipartFile avatar,
                                             @RequestParam("changeavatar") String changeavatar,
                                             @RequestParam("lastname") String lastname,
                                             @RequestParam("firstname") String firstname,
                                             @RequestParam("birthday") String birthday,
                                             @RequestParam("gender") String gender,
                                             @RequestParam("status") String status,
                                             @RequestParam("phone") String phone,
                                             @RequestParam("email") String email,
                                             @RequestParam("town") String town,
                                             @RequestParam("address") String address,
                                             @RequestParam("roles") String[] roles,
                                             HttpServletResponse servletResponse) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/account/edit/idAcc=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String error = "";
        Boolean g;
        if (gender.equals("1")) {
            g = true;
        } else g = false;
        Boolean e;
        if (status.equals("1")) {
            e = true;
        } else e = false;
        if (listAllAccount == null) {
            listAllAccount = new ArrayList<>();
            String urlGetAllAccount = "http://localhost:8080/api/acc/getAllAccount";
            ResponseEntity<AccountDTO[]> reponseGetAllAccount = restTemplate.getForEntity(urlGetAllAccount, AccountDTO[].class);
            Collections.addAll(listAllAccount, reponseGetAllAccount.getBody());
            Collections.sort(listAllAccount, new Comparator<AccountDTO>() {
                @Override
                public int compare(AccountDTO o1, AccountDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
            for (AccountDTO acc : listAllAccount) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", acc.getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                acc.setAvatar(response.getBody().getUrl());
            }
        }

        AccountDTO oldAcc = new AccountDTO();
        for (AccountDTO acc : listAllAccount) {
            if (acc.getId() == id) {
                oldAcc = acc;
            }
        }

        AccountDTO editAcc;
        List<String> rolesOnAccount = new ArrayList<>();
        Collections.addAll(rolesOnAccount, roles);
        if (changeavatar.equals("false")) {
            editAcc = new AccountDTO(oldAcc.getId(), oldAcc.getUsername(), oldAcc.getPassword(), e, email, changeavatar, firstname, lastname, inputDateFormat.parse(birthday), Integer.parseInt(town), address, phone, g, rolesOnAccount);
        } else {
            String fs = avatar.getOriginalFilename();
            String filenameL = fs.substring(fs.indexOf("."), fs.length());
            editAcc = new AccountDTO(oldAcc.getId(), oldAcc.getUsername(), oldAcc.getPassword(), e, email, "./image/account/user-" + oldAcc.getId() + filenameL, firstname, lastname, inputDateFormat.parse(birthday), Integer.parseInt(town), address, phone, g, rolesOnAccount);
        }

        for (AccountDTO acc : listAllAccount) {
            if ((acc.getEmail().equals(email)) && (acc.getId() != id)) {
                error += "Email đã được sử dụng!";
                break;
            }
        }

        if (error.equals("")) {
            error += "Sửa tài khoản thành công!";
            String urlEditAccount = "http://localhost:8080/api/acc/edit";
            HttpEntity<AccountDTO> requestEditAccount = new HttpEntity<>(editAcc);
            ResponseEntity<AccountDTO> responseEditAccount = restTemplate.exchange(urlEditAccount, HttpMethod.PUT, requestEditAccount, AccountDTO.class);
            listAllAccount.set(listAllAccount.indexOf(oldAcc), responseEditAccount.getBody());
            if (changeavatar.equals("false")) {
                editAcc.setAvatar(oldAcc.getAvatar());
                listAllAccount.get(listAllAccount.indexOf(responseEditAccount.getBody())).setAvatar(oldAcc.getAvatar());
            } else {
                HttpHeaders headersUploadImage = new HttpHeaders();
                headersUploadImage.setContentType(MediaType.MULTIPART_FORM_DATA);
                Resource muti = avatar.getResource();
                MultiValueMap<String, Object> mapUp = new LinkedMultiValueMap<String, Object>();
                mapUp.add("image", muti);
                HttpEntity<MultiValueMap<String, Object>> requestUp = new HttpEntity<>(mapUp, headersUploadImage);
                String urlUpdateImage = "http://localhost:8080/uploadimage/" + "user-" + id + "/121";
                restTemplate.postForEntity(urlUpdateImage, requestUp, JSONObject.class);

                HttpHeaders headersGetImage = new HttpHeaders();
                headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("url", responseEditAccount.getBody().getAvatar());
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersGetImage);
                String urlImage = "http://localhost:8080/getImage";
                ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
                editAcc.setAvatar(response.getBody().getUrl());
                listAllAccount.get(listAllAccount.indexOf(responseEditAccount.getBody())).setAvatar(response.getBody().getUrl());
            }
        }

        String urlTown = "http://localhost:8080/api/address/getTownById/" + editAcc.getTown();
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

        Map<String, Integer> mapRolesOnAccount = new TreeMap<>();
        mapRolesOnAccount.put("User", 0);
        mapRolesOnAccount.put("Admin", 0);
        for (String role : editAcc.getRoles()) {
            if (role.equals("user")) {
                mapRolesOnAccount.put("User", 1);
            }
            if (role.equals("admin")) {
                mapRolesOnAccount.put("Admin", 1);
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("mapRolesOnAccount", mapRolesOnAccount);
        model.addAttribute("accTown", responseTown.getBody().getId());
        model.addAttribute("accDistrict", responseDistrict.getBody().getId());
        model.addAttribute("accCity", responseDistrict.getBody().getCityId());
        model.addAttribute("listTownByDistrict", listTownByDistrict);
        model.addAttribute("listDistrictByCity", listDistrictByCity);
        model.addAttribute("listCity", listCity);
        model.addAttribute("editAcc", editAcc);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        model.addAttribute("error", error);
        return new ModelAndView("admin/edituser");
    }
}
