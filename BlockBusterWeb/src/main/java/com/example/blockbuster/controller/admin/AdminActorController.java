package com.example.blockbuster.controller.admin;

import com.example.blockbuster.apicall.DataCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.CastDTO;
import com.example.blockbuster.dto.ImageDTO;
import com.example.blockbuster.dto.LoginResponse;
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
public class AdminActorController {
    ArrayList<CastDTO> listAllCast;
    Boolean isEdit = false;
    Boolean isView = false;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("actor")
    public ModelAndView adminActorShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String err = "";
        RestTemplate restTemplate = new RestTemplate();
        isEdit = false;
        isView = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/actor");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllCast == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.reverse(listAllCast);
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("err", err);
        model.addAttribute("isView", isView);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("format", format);
        model.addAttribute("listAllCast", listAllCast);
        return new ModelAndView("admin/actor");
    }

    @RequestMapping("actor/view/idAct={id}")
    public ModelAndView adminActorView(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = false;
        isView = true;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/actor/view/idAct=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        CastDTO cast = new CastDTO();

        if (listAllCast == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.reverse(listAllCast);
        }

        for (CastDTO loopDir : listAllCast) {
            if (loopDir.getId() == id) {
                cast = loopDir;
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("err", err);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("isView", isView);
        model.addAttribute("viewCast", cast);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("format", format);
        model.addAttribute("listAllCast", listAllCast);
        return new ModelAndView("admin/actor");
    }

    @RequestMapping(method = RequestMethod.POST, path = "actor/add")
    public ModelAndView adminAddActor(Model model, HttpSession session,
                                      @RequestParam("name") String name,
                                      @RequestParam("avatar") MultipartFile avatar,
                                      @RequestParam("birthday") String birthday,
                                      @RequestParam("story") String story,
                                      HttpServletResponse servletResponse) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/actor");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String err = "";
        isEdit = false;
        isView = false;
        String fs = avatar.getOriginalFilename();
        String filenameL = fs.substring(fs.indexOf("."), fs.length());

        CastDTO cast = new CastDTO(0, filenameL, name, story, new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        String urlAddDir = "http://localhost:8080/api/cast/create";
        HttpEntity<CastDTO> requestAddActor = new HttpEntity<>(cast);
        ResponseEntity<CastDTO> responseAddActor = restTemplate.postForEntity(urlAddDir, requestAddActor, CastDTO.class);
        listAllCast.add(0, responseAddActor.getBody());

        HttpHeaders headersUploadImage = new HttpHeaders();
        headersUploadImage.setContentType(MediaType.MULTIPART_FORM_DATA);
        Resource muti = avatar.getResource();
        MultiValueMap<String, Object> mapUp = new LinkedMultiValueMap<String, Object>();
        mapUp.add("image", muti);
        HttpEntity<MultiValueMap<String, Object>> requestUp = new HttpEntity<>(mapUp, headersUploadImage);
        String urlUpdateImage = "http://localhost:8080/uploadimage/" + "act-" + responseAddActor.getBody().getId() + "/231";
        restTemplate.postForEntity(urlUpdateImage, requestUp, JSONObject.class);

        HttpHeaders headersGetImage = new HttpHeaders();
        headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("url", listAllCast.get(0).getAvatar());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersGetImage);
        String urlImage = "http://localhost:8080/getImage";
        ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
        listAllCast.get(0).setAvatar(response.getBody().getUrl());
        err += "Thêm diễn viên thành công!!!";
        if (listAllCast == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.reverse(listAllCast);
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("err", err);
        model.addAttribute("isView", isView);
        model.addAttribute("viewCast", cast);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("format", format);
        model.addAttribute("listAllCast", listAllCast);
        return new ModelAndView("admin/actor");
    }

    @RequestMapping(path = "actor/delete/idAct={id}")
    public ModelAndView adminDeleteDirector(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = false;
        isView = false;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/actor");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllCast == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.reverse(listAllCast);
        }

        err += "Xoá diễn viên thành công!!!";
        String urlDeleteCast = "http://localhost:8080/api/cast/remove/" + id;
        restTemplate.getForEntity(urlDeleteCast, String.class);
        for (CastDTO cast : listAllCast) {
            if (cast.getId() == id) {
                listAllCast.remove(cast);
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("err", err);
        model.addAttribute("isView", isView);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("format", format);
        model.addAttribute("listAllCast", listAllCast);

        return new ModelAndView("admin/actor");
    }

    @RequestMapping(path = "actor/edit/idAct={id}")
    public ModelAndView adminEditActorShow(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/actor/edit/idAct=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String err = "";
        isEdit = true;
        isView = false;
        CastDTO castEdit = new CastDTO();
        if (listAllCast == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.reverse(listAllCast);
        }

        for (CastDTO cast : listAllCast) {
            if (cast.getId() == id) {
                castEdit = cast;
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("castEdit", castEdit);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isView", isView);
        model.addAttribute("err", err);
        model.addAttribute("listAllCast", listAllCast);

        return new ModelAndView("admin/actor");
    }

    @RequestMapping(method = RequestMethod.POST, path = "actor/edit/idAct={id}")
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
            session.setAttribute("oldAdminUrl", "/admin/actor/edit/idAct=" + id);
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

        CastDTO castEdit = new CastDTO();
        if (listAllCast == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllCast = (ArrayList<CastDTO>) session.getAttribute("listCast");
            Collections.reverse(listAllCast);
        }

        for (CastDTO cast : listAllCast) {
            if (cast.getId() == id) {
                castEdit = cast;
                break;
            }
        }

        err += "Sửa diễn viên thành công!!!";
        String urlEditCast = "http://localhost:8080/api/cast/edit";
        CastDTO castDTO;
        if (avtchange.equals("true")) {
            castDTO = new CastDTO(id, filenameL, name, story, dateInputFormat.parse(birthday));
        } else castDTO = new CastDTO(id, "false", name, story, dateInputFormat.parse(birthday));
        HttpEntity<CastDTO> requestEditCast = new HttpEntity<>(castDTO);
        ResponseEntity<CastDTO> responseEditCast = restTemplate.postForEntity(urlEditCast, requestEditCast, CastDTO.class);

        if (avtchange.equals("true")) {
            HttpHeaders headersUploadImage = new HttpHeaders();
            headersUploadImage.setContentType(MediaType.MULTIPART_FORM_DATA);
            Resource muti = avatar.getResource();
            MultiValueMap<String, Object> mapUp = new LinkedMultiValueMap<String, Object>();
            mapUp.add("image", muti);
            HttpEntity<MultiValueMap<String, Object>> requestUp = new HttpEntity<>(mapUp, headersUploadImage);
            String urlUpdateImage = "http://localhost:8080/uploadimage/" + "act-" + id + "/231";
            restTemplate.postForEntity(urlUpdateImage, requestUp, JSONObject.class);

            HttpHeaders headersGetImage = new HttpHeaders();
            headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", responseEditCast.getBody().getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersGetImage);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> reponseGet = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            castDTO.setAvatar(reponseGet.getBody().getUrl());
        } else castDTO.setAvatar(castEdit.getAvatar());
        listAllCast.set(listAllCast.indexOf(castEdit), castDTO);

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("dateInputFormat", dateInputFormat);
        model.addAttribute("castEdit", castDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isView", isView);
        model.addAttribute("err", err);
        model.addAttribute("listAllCast", listAllCast);

        return new ModelAndView("admin/actor");
    }
}
