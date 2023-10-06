package com.example.blockbuster.controller.admin;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.LoginResponse;
import com.example.blockbuster.dto.MembershipDTO;
import com.example.blockbuster.dto.MembershipDetailDTO;
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
public class AdminMembershipDetailController {
    ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
    Boolean isEdit = false;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("memDe")
    public ModelAndView adminMembershipShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/memDe");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMembership.size() == 0) {
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("format", format);
        model.addAttribute("listAllMembership", listAllMembership);
        return new ModelAndView("admin/membershipforpack");
    }

    @RequestMapping("memDe/idMembership={id}")
    public ModelAndView adminMembershipPackageShow(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/memDe/idMembership=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");
        if (listAllMembership.size() == 0) {
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        MembershipDTO membershipDTO = new MembershipDTO();
        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == id) {
                membershipDTO = mem;
            }
        }


        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("membershipDTO", membershipDTO);
        model.addAttribute("format", format);
        model.addAttribute("isEdit", isEdit);
        return new ModelAndView("admin/membershipdetail");
    }

    @RequestMapping(method = RequestMethod.POST, path = "memDe/add/idMembership={id}")
    public ModelAndView adminAddMembershipPackage(Model model, HttpSession session, @PathVariable(value = "id") int membershipId
            , @RequestParam(value = "date") String day
            , @RequestParam(value = "month") String month
            , @RequestParam(value = "year") String year
            , @RequestParam(value = "price") String price
            , HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        boolean check = true;
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/memDe/idMembership=" + membershipId);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMembership.size() == 0) {
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        MembershipDTO membershipDTO = new MembershipDTO();
        int index = -1;
        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == membershipId) {
                membershipDTO = mem;
                index = listAllMembership.indexOf(mem);
                break;
            }
        }

        err += MessageUtil.VALIDATION_MEMBERSHIPDETAIL_ADD_SUCCESS;
        String urlAddMembershipDetail = "http://localhost:8080/api/membership-detail/add";
        MembershipDetailDTO membershipDetailDTO = new MembershipDetailDTO(0, Integer.parseInt(year.equals("")?"0":year), Integer.parseInt(month.equals("")?"0":month), Integer.parseInt(day.equals("")?"0":day), Integer.parseInt(price.equals("")?"0":price), membershipDTO.getId());
        HttpEntity<MembershipDetailDTO> requestAddMembershipDetail = new HttpEntity<>(membershipDetailDTO);
        ResponseEntity<MembershipDetailDTO> responseAddMembershipDetail = restTemplate.postForEntity(urlAddMembershipDetail, requestAddMembershipDetail, MembershipDetailDTO.class);
        ArrayList<MembershipDetailDTO> tmpList = (ArrayList<MembershipDetailDTO>) membershipDTO.getMembershipDetails();
        tmpList.add(responseAddMembershipDetail.getBody());
        membershipDTO.setMembershipDetails(tmpList);
        listAllMembership.set(index, membershipDTO);

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("membershipDTO", membershipDTO);

        return new ModelAndView("admin/membershipdetail");
    }

    @RequestMapping(path = "memDe/delete/idMembership={idMembership}&idMemDe={idMemDe}")
    public ModelAndView adminDeleteMembershipPackage(Model model, HttpSession session, @PathVariable(value = "idMembership") int idMembership,
                                                     @PathVariable(value = "idMemDe") int idMemDe,
                                                     HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = false;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/memDe/idMembership=" + idMembership);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if(listAllMembership.size() == 0){
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }
        MembershipDTO membershipDTO = new MembershipDTO();
        int indexMem = -1;
        int indexMemDe = -1;
        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == idMembership) {
                membershipDTO = mem;
                indexMem = listAllMembership.indexOf(mem);
                for (MembershipDetailDTO memDe: mem.getMembershipDetails()) {
                    if(memDe.getId() == idMemDe){
                        indexMemDe = mem.getMembershipDetails().indexOf(memDe);
                        break;
                    }
                }
                break;
            }
        }

        err += MessageUtil.VALIDATION_MEMBERSHIPDETAIL_DELETE_SUCCESS;
        String urlDeleteMembershipPackage = "http://localhost:8080/api/membership-detail/remove/" + idMemDe;
        restTemplate.getForEntity(urlDeleteMembershipPackage, String.class);

        ArrayList<MembershipDetailDTO> tmpList = (ArrayList<MembershipDetailDTO>) membershipDTO.getMembershipDetails();
        tmpList.remove(indexMemDe);
        membershipDTO.setMembershipDetails(tmpList);
        listAllMembership.set(indexMem, membershipDTO);

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);
        model.addAttribute("membershipDTO", membershipDTO);
        return new ModelAndView("admin/membershipdetail");
    }

    @RequestMapping(path = "memDe/edit/idMembership={idMembership}&idMemDe={idMemDe}")
    public ModelAndView adminEditMembershipDeShow(Model model, HttpSession session,@PathVariable(value = "idMembership") int idMembership,
                                                @PathVariable(value = "idMemDe") int idMemDe, HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = true;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/memDe/edit/idMembership=" + idMembership + "&idMemDe=" + idMemDe);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if(listAllMembership.size() == 0){
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        MembershipDetailDTO memDeEdit = new MembershipDetailDTO();
        MembershipDTO membershipDTO = new MembershipDTO();
        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == idMembership) {
                membershipDTO = mem;
                for (MembershipDetailDTO memDe: mem.getMembershipDetails()) {
                    if(memDe.getId() == idMemDe){
                        memDeEdit = memDe;
                        break;
                    }
                }
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("memDeEdit", memDeEdit);
        model.addAttribute("membershipDTO", membershipDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);

        return new ModelAndView("admin/membershipdetail");
    }

    @RequestMapping(method = RequestMethod.POST, path = "memDe/edit/idMembership={idMembership}&idMemDe={idMemDe}")
    public ModelAndView adminEditMembershipDePost(Model model, HttpSession session, @PathVariable(value = "idMembership") int idMembership, @PathVariable(value = "idMemDe") int idMemDe
                                                , @RequestParam(value = "date") String day
                                                , @RequestParam(value = "month") String month
                                                , @RequestParam(value = "year") String year
                                                , @RequestParam(value = "price") String price
                                                , HttpServletResponse servletResponse) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String err = "";
        isEdit = true;

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/memDe/edit/idMembership=" + idMembership + "&idMemDe=" + idMemDe);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if(listAllMembership.size() == 0){
            String urlAllMembership = "http://localhost:8080/api/membership/getAll";
            ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
            Collections.addAll(listAllMembership, responseAllMembership.getBody());
        }

        MembershipDetailDTO memDeEdit = new MembershipDetailDTO();
        MembershipDTO membershipDTO = new MembershipDTO();
        int indexMem = -1;
        int indexMemDe = -1;
        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == idMembership) {
                membershipDTO = mem;
                indexMem = listAllMembership.indexOf(mem);
                for (MembershipDetailDTO memDe: mem.getMembershipDetails()) {
                    if(memDe.getId() == idMemDe){
                        memDeEdit = memDe;
                        indexMemDe = mem.getMembershipDetails().indexOf(memDe);
                        break;
                    }
                }
                break;
            }
        }

        err += MessageUtil.VALIDATION_MEMBERSHIPDETAIL_UPDATE_SUCCESS;
        String urlEditMembershipDetail = "http://localhost:8080/api/membership-detail/edit";
        MembershipDetailDTO membershipDetailDTO = new MembershipDetailDTO(idMemDe, Integer.parseInt(year.equals("")?"0":year), Integer.parseInt(month.equals("")?"0":month), Integer.parseInt(day.equals("")?"0":day), Integer.parseInt(price.equals("")?"0":price), membershipDTO.getId());
        HttpEntity<MembershipDetailDTO> requestEditMembershipDetail = new HttpEntity<>(membershipDetailDTO);
        ResponseEntity<MembershipDetailDTO> responseEditMembershipDetail = restTemplate.postForEntity(urlEditMembershipDetail, requestEditMembershipDetail, MembershipDetailDTO.class);
        ArrayList<MembershipDetailDTO> tmpList = (ArrayList<MembershipDetailDTO>) membershipDTO.getMembershipDetails();
        tmpList.set(indexMemDe, responseEditMembershipDetail.getBody());
        membershipDTO.setMembershipDetails(tmpList);
        listAllMembership.set(indexMem, membershipDTO);

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("memDeEdit", memDeEdit);
        model.addAttribute("membershipDTO", membershipDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("err", err);

        return new ModelAndView("admin/membershipdetail");
    }
}
