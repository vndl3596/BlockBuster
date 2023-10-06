package com.example.blockbuster.controller.admin;

import com.example.blockbuster.apicall.DataCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.DirectorDTO;
import com.example.blockbuster.dto.ImageDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.util.MessageUtil;
import org.json.simple.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/admin/")
public class AdminDirectorController {
    ArrayList<DirectorDTO> listAllDir;
    Boolean isEdit = false;
    Boolean isView = false;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("director")
    public ModelAndView adminDirectorShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String err = "";
        RestTemplate restTemplate = new RestTemplate();
        isEdit = false;
        isView = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/director");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllDir == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
            Collections.reverse(listAllDir);
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("err", err);
        model.addAttribute("isView", isView);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("format", format);
        model.addAttribute("listAllDir", listAllDir);
        return new ModelAndView("admin/director");
    }

    @RequestMapping("director/view/idDir={id}")
    public ModelAndView adminDirectorView(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = false;
        isView = true;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/director/view/idDir=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        DirectorDTO dir = new DirectorDTO();

        if (listAllDir == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
            Collections.reverse(listAllDir);
        }

        for (DirectorDTO loopDir : listAllDir) {
            if (loopDir.getId() == id) {
                dir = loopDir;
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("err", err);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("isView", isView);
        model.addAttribute("viewDir", dir);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("format", format);
        model.addAttribute("listAllDir", listAllDir);
        return new ModelAndView("admin/director");
    }

    @RequestMapping(method = RequestMethod.POST, path = "director/add")
    public ModelAndView adminAddDirector(Model model, HttpSession session,
                                         @RequestParam("name") String name,
                                         @RequestParam("avatar") MultipartFile avatar,
                                         @RequestParam("birthday") String birthday,
                                         @RequestParam("story") String story,
                                         HttpServletResponse servletResponse) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = false;
        isView = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/director");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String fs = avatar.getOriginalFilename();
        String filenameL = fs.substring(fs.indexOf("."), fs.length());

        DirectorDTO dir = new DirectorDTO(0, filenameL, name, story, new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        String urlAddDir = "http://localhost:8080/api/director/create";
        HttpEntity<DirectorDTO> requestAddDir = new HttpEntity<>(dir);
        ResponseEntity<DirectorDTO> responseAddDir = restTemplate.postForEntity(urlAddDir, requestAddDir, DirectorDTO.class);
        listAllDir.add(0, responseAddDir.getBody());

        HttpHeaders headersUploadImage = new HttpHeaders();
        headersUploadImage.setContentType(MediaType.MULTIPART_FORM_DATA);
        Resource muti = avatar.getResource();
        MultiValueMap<String, Object> mapUp = new LinkedMultiValueMap<String, Object>();
        mapUp.add("image", muti);
        HttpEntity<MultiValueMap<String, Object>> requestUp = new HttpEntity<>(mapUp, headersUploadImage);
        String urlUpdateImage = "http://localhost:8080/uploadimage/" + "dir-" + responseAddDir.getBody().getId() + "/241";
        restTemplate.postForEntity(urlUpdateImage, requestUp, JSONObject.class);

        HttpHeaders headersGetImage = new HttpHeaders();
        headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("url", listAllDir.get(0).getAvatar());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersGetImage);
        String urlImage = "http://localhost:8080/getImage";
        ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
        listAllDir.get(0).setAvatar(response.getBody().getUrl());
        err += MessageUtil.VALIDATION_DIRECTOR_ADD_SUCCESS;
        if (listAllDir == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
            Collections.reverse(listAllDir);
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("err", err);
        model.addAttribute("isView", isView);
        model.addAttribute("viewDir", dir);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("format", format);
        model.addAttribute("listAllDir", listAllDir);
        return new ModelAndView("admin/director");
    }

    @RequestMapping(path = "director/delete/idDir={id}")
    public ModelAndView adminDeleteDirector(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = false;
        isView = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/director");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        if (listAllDir == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
            Collections.reverse(listAllDir);
        }

        err += MessageUtil.VALIDATION_DIRECTOR_DELETE_SUCCESS;
        String urlDeleteDirector = "http://localhost:8080/api/director/remove/" + id;
        restTemplate.getForEntity(urlDeleteDirector, String.class);
        for (DirectorDTO dir : listAllDir) {
            if (dir.getId() == id) {
                listAllDir.remove(dir);
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("err", err);
        model.addAttribute("isView", isView);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("format", format);
        model.addAttribute("listAllDir", listAllDir);

        return new ModelAndView("admin/director");
    }

    @RequestMapping(path = "director/edit/idDir={id}")
    public ModelAndView adminEditDirectorShow(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");

        String err = "";
        isEdit = true;
        isView = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/edit/idDir=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        DirectorDTO dirEdit = new DirectorDTO();
        if (listAllDir == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
            Collections.reverse(listAllDir);
        }

        for (DirectorDTO dir : listAllDir) {
            if (dir.getId() == id) {
                dirEdit = dir;
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("dirEdit", dirEdit);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isView", isView);
        model.addAttribute("err", err);
        model.addAttribute("listAllDir", listAllDir);

        return new ModelAndView("admin/director");
    }

    @RequestMapping(method = RequestMethod.POST, path = "director/edit/idDir={id}")
    public ModelAndView adminEditDirectorPost(Model model, HttpSession session,
                                              @PathVariable(value = "id") int id,
                                              @RequestParam("name") String name,
                                              @RequestParam("avatar") MultipartFile avatar,
                                              @RequestParam("birthday") String birthday,
                                              @RequestParam("story") String story,
                                              @RequestParam("avtchange") String avtchange,
                                              HttpServletResponse servletResponse) throws ParseException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String err = "";
        isEdit = true;
        isView = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/director/edit/idDir=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String fs = "";
        String filenameL = "";

        if (avtchange.equals("true")) {
            fs = avatar.getOriginalFilename();
            filenameL = fs.substring(fs.indexOf("."), fs.length());
        }

        DirectorDTO dirEdit = new DirectorDTO();
        if (listAllDir == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllDir = (ArrayList<DirectorDTO>) session.getAttribute("listDirector");
            Collections.reverse(listAllDir);
        }

        for (DirectorDTO dir : listAllDir) {
            if (dir.getId() == id) {
                dirEdit = dir;
                break;
            }
        }

        err += MessageUtil.VALIDATION_DIRECTOR_UPDATE_SUCCESS;
        String urlEditDirector = "http://localhost:8080/api/director/edit";
        DirectorDTO directorDTO;
        if (avtchange.equals("true")) {
            directorDTO = new DirectorDTO(id, filenameL, name, story, dateInputFormat.parse(birthday));
        } else directorDTO = new DirectorDTO(id, "false", name, story, dateInputFormat.parse(birthday));
        HttpEntity<DirectorDTO> requestEditDirector = new HttpEntity<>(directorDTO);
        ResponseEntity<DirectorDTO> responseEditDirector = restTemplate.postForEntity(urlEditDirector, requestEditDirector, DirectorDTO.class);

        if (avtchange.equals("true")) {
            HttpHeaders headersUploadImage = new HttpHeaders();
            headersUploadImage.setContentType(MediaType.MULTIPART_FORM_DATA);
            Resource muti = avatar.getResource();
            MultiValueMap<String, Object> mapUp = new LinkedMultiValueMap<String, Object>();
            mapUp.add("image", muti);
            HttpEntity<MultiValueMap<String, Object>> requestUp = new HttpEntity<>(mapUp, headersUploadImage);
            String urlUpdateImage = "http://localhost:8080/uploadimage/" + "dir-" + id + "/241";
            ResponseEntity<JSONObject> responseUpdateImage = restTemplate.postForEntity(urlUpdateImage, requestUp, JSONObject.class);

            HttpHeaders headersGetImage = new HttpHeaders();
            headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", responseEditDirector.getBody().getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersGetImage);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> reponseGet = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            directorDTO.setAvatar(reponseGet.getBody().getUrl());
        } else directorDTO.setAvatar(dirEdit.getAvatar());
        listAllDir.set(listAllDir.indexOf(dirEdit), directorDTO);

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("dirEdit", directorDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isView", isView);
        model.addAttribute("err", err);
        model.addAttribute("listAllDir", listAllDir);

        return new ModelAndView("admin/director");
    }
}
