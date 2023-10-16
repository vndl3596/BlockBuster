package com.example.blockbuster.controller.admin;

import com.example.blockbuster.apicall.DataCall;
import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.GenreDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MembershipDTO;
import com.example.blockbuster.util.MessageUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/admin/")
public class AdminMembershipController {
    ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
    Boolean isEdit = false;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("membership")
    public ModelAndView adminMembershipShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/membership");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if(listAllMembership.size() == 0){
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("format", format);
        model.addAttribute("listAllMembership", listAllMembership);
        return new ModelAndView("admin/membership");
    }

    @RequestMapping(method = RequestMethod.POST, path = "membership/add")
    public ModelAndView adminAddMembership(Model model, HttpSession session, @RequestParam(value = "membershipName") String membershipName
            , @RequestParam(value = "membershipDetail") String membershipDetail
            ,HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        boolean check = true;
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/membership");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if(listAllMembership.size() == 0){
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        for (MembershipDTO mem: listAllMembership) {
            if(mem.getName().equalsIgnoreCase(membershipName)){
                check = false;
            }
        }

        if (check == true) {
            err += MessageUtil.VALIDATION_MEMBERSHIP_ADD_SUCCESS;
            String urlAddMembership = "http://localhost:8080/api/membership/add";
            MembershipDTO membershipDTO = new MembershipDTO(0, membershipName.toUpperCase().replaceAll(" ", ""), membershipDetail, null);
            HttpEntity<MembershipDTO> requestAddMembership = new HttpEntity<>(membershipDTO);
            ResponseEntity<MembershipDTO> responseAddGenre = restTemplate.postForEntity(urlAddMembership, requestAddMembership, MembershipDTO.class);
            listAllMembership.add(0, responseAddGenre.getBody());
        } else err += MessageUtil.VALIDATION_MEMBERSHIP_AU_ERR01;

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("listAllMembership", listAllMembership);

        return new ModelAndView("admin/membership");
    }

    @RequestMapping(path = "membership/delete/idMembership={id}")
    public ModelAndView adminDeleteGenre(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/membership");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if(listAllMembership.size() == 0){
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        err += MessageUtil.VALIDATION_MEMBERSHIP_DELETE_SUCCESS;
        String urlDeleteMembership = "http://localhost:8080/api/membership/remove/" + id;
        restTemplate.getForEntity(urlDeleteMembership, String.class);
        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == id) {
                listAllMembership.remove(mem);
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("listAllMembership", listAllMembership);

        return new ModelAndView("admin/membership");
    }

    @RequestMapping(path = "membership/edit/idMembership={id}")
    public ModelAndView adminEditMembershipShow(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = true;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/membership/edit/idMembership=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        MembershipDTO membershipEdit = new MembershipDTO();
        if(listAllMembership.size() == 0){
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == id) {
                membershipEdit = mem;
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("membershipEdit", membershipEdit);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("listAllMembership", listAllMembership);

        return new ModelAndView("admin/membership");
    }

    @RequestMapping(method = RequestMethod.POST, path = "membership/edit/idMembership={id}")
    public ModelAndView adminEditMembershipPost(Model model, HttpSession session, @PathVariable(value = "id") int id,
                                                @RequestParam(value = "membershipName") String membershipName,
                                                @RequestParam(value = "membershipDetail") String membershipDetail,
                                                HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = true;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/membership/edit/idMembership=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        boolean check = true;
        MembershipDTO membershipEdit = new MembershipDTO();
        if(listAllMembership.size() == 0){
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == id) {
                membershipEdit = mem;
                break;
            }
        }

        for (MembershipDTO mem : listAllMembership) {
            if ((mem.getId() != id) && (mem.getName().equalsIgnoreCase(membershipName))) {
                check = false;
                break;
            }
        }

        if (check == true) {
            err += MessageUtil.VALIDATION_MEMBERSHIP_UPDATE_SUCCESS;
            String urlEditMembership = "http://localhost:8080/api/membership/edit";
            MembershipDTO membershipDTO = new MembershipDTO(id, membershipName.toUpperCase().replaceAll(" ", ""), membershipDetail, null);
            HttpEntity<MembershipDTO> requestEditMembership = new HttpEntity<>(membershipDTO);
            restTemplate.postForEntity(urlEditMembership, requestEditMembership, MembershipDTO.class);
            listAllMembership.set(listAllMembership.indexOf(membershipEdit), membershipDTO);
            membershipEdit.setName(membershipName);
            membershipEdit.setDetail(membershipDetail);
        } else err += MessageUtil.VALIDATION_MEMBERSHIP_AU_ERR01;

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("membershipEdit", membershipEdit);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("listAllMembership", listAllMembership);

        return new ModelAndView("admin/membership");
    }
}
