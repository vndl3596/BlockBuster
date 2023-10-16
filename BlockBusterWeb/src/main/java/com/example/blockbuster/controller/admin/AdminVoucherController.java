package com.example.blockbuster.controller.admin;

import com.example.blockbuster.apicall.DataCall;
import com.example.blockbuster.dto.*;
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
import java.util.*;

@RestController
@RequestMapping("/admin/")
public class AdminVoucherController {
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("voucher")
    public ModelAndView adminVoucherShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/voucher");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String urlAllVoucher = "http://localhost:8080/api/voucher/getAll";
        ArrayList<VoucherDTO> voucherList = new ArrayList<>();
        Collections.addAll(voucherList, restTemplate.getForEntity(urlAllVoucher, VoucherDTO[].class).getBody());

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("formatter", formatter);
        model.addAttribute("voucherList", voucherList);
        return new ModelAndView("admin/voucher");
    }

    @RequestMapping("voucher/view/idVou={id}")
    public ModelAndView adminVoucherViewById(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/voucher/view/idVou=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String urlGetVoucherById = "http://localhost:8080/api/voucher/getById/idVoucher=" + id;
        VoucherDTO viewVoucher = restTemplate.getForEntity(urlGetVoucherById, VoucherDTO.class).getBody();

        String urlGetVoucherMembership = "http://localhost:8080/api/membership/" + viewVoucher.getPackageReduce().getMembership();
        MembershipDTO membershipOfVoucher = restTemplate.getForEntity(urlGetVoucherMembership, MembershipDTO.class).getBody();

        MembershipDetailDTO membershipDetailOfVoucher = new MembershipDetailDTO();
        for (MembershipDetailDTO memberDetail: membershipOfVoucher.getMembershipDetails()) {
            if(memberDetail.getId() == viewVoucher.getPackageReduce().getId()){
                membershipDetailOfVoucher = memberDetail;
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("viewVoucher", viewVoucher);
        model.addAttribute("membershipOfVoucher", membershipOfVoucher);
        model.addAttribute("membershipDetailOfVoucher", membershipDetailOfVoucher);
        model.addAttribute("format", format);
        model.addAttribute("inputDateTimeFormat", inputDateTimeFormat);
        return new ModelAndView("admin/viewvoucher");
    }

    @RequestMapping("voucher/add")
    public ModelAndView adminMovieAddGet(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String error = "";

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/voucher/add");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        RestTemplate restTemplate = new RestTemplate();
        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", error);
        model.addAttribute("format", format);
        model.addAttribute("inputDateTimeFormat", inputDateTimeFormat);
        model.addAttribute("listAllMembership", listAllMembership);
        return new ModelAndView("admin/addvoucher");
    }

    @RequestMapping(method = RequestMethod.POST, path = "voucher/add")
    public ModelAndView adminVoucherAddPost(Model model, HttpSession session,
                                          @RequestParam("voucherCode") String voucherCode,
                                          @RequestParam("voucherName") String voucherName,
                                          @RequestParam("timeFrom") String timeFrom,
                                          @RequestParam("timeTo") String timeTo,
                                          @RequestParam("status") String status,
                                          @RequestParam("reduce") String reduce,
                                          @RequestParam("type") String type,
                                          @RequestParam("membershipDetail") String membershipDetail,
                                          HttpServletResponse servletResponse) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String error = "";
        RestTemplate restTemplate = new RestTemplate();
        boolean stt = false;
        if (status.equals("1")) {
            stt = true;
        }

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/voucher/add");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        VoucherDTO voucher = new VoucherDTO(0, voucherCode, voucherName, inputDateTimeFormat.parse(timeFrom), inputDateTimeFormat.parse(timeTo), stt, Integer.parseInt(reduce), Integer.parseInt(type), new MembershipDetailDTO(Integer.parseInt(membershipDetail), 0, 0, 0, 0, 0));
        String urlAddVoucher = "http://localhost:8080/api/voucher/addVoucher";
        HttpEntity<VoucherDTO> requestAddVoucher = new HttpEntity<>(voucher);
        ResponseEntity<VoucherDTO> responseAddVoucher = restTemplate.postForEntity(urlAddVoucher, requestAddVoucher, VoucherDTO.class);
        if(responseAddVoucher.getBody().getVoucherCode() != null){
            error += MessageUtil.VALIDATION_VOUCHER_ADD_SUCCESS;
        }
        else{
            error += MessageUtil.VALIDATION_VOUCHER_AU_ERR01;
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", error);
        model.addAttribute("format", format);
        model.addAttribute("inputDateTimeFormat", inputDateTimeFormat);
        model.addAttribute("listAllMembership", listAllMembership);
        return new ModelAndView("admin/addvoucher");
    }

    @RequestMapping("voucher/edit/idVou={id}")
    public ModelAndView adminVoucherEditGet(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        RestTemplate restTemplate = new RestTemplate();
        String error = "";

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/voucher/edit/idVou=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String urlAllVoucher = "http://localhost:8080/api/voucher/getAll";
        ArrayList<VoucherDTO> voucherList = new ArrayList<>();
        Collections.addAll(voucherList, restTemplate.getForEntity(urlAllVoucher, VoucherDTO[].class).getBody());

        VoucherDTO editV = new VoucherDTO();
        for (VoucherDTO v : voucherList) {
            if (v.getId() == id) {
                editV = v;
            }
        }
        MembershipDetailDTO oldMembershipDetail = editV.getPackageReduce();

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        MembershipDTO oldMembership = new MembershipDTO();
        for (MembershipDTO mem : listAllMembership) {
            if(mem.getId() == oldMembershipDetail.getMembership()){
                oldMembership = mem;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", error);
        model.addAttribute("listAllMembership", listAllMembership);
        model.addAttribute("editV", editV);
        model.addAttribute("oldMembership", oldMembership);
        model.addAttribute("oldMembershipDetails", oldMembership.getMembershipDetails());
        model.addAttribute("oldMembershipDetail", oldMembershipDetail);
        model.addAttribute("format", format);
        model.addAttribute("inputDateTimeFormat", inputDateTimeFormat);
        return new ModelAndView("admin/editvoucher");
    }

    @RequestMapping(method = RequestMethod.POST, path = "voucher/edit/idVou={id}")
    public ModelAndView adminVoucherEditPost(Model model, HttpSession session, @PathVariable("id") int id,
                                              @RequestParam("voucherName") String voucherName,
                                              @RequestParam("timeFrom") String timeFrom,
                                              @RequestParam("timeTo") String timeTo,
                                              @RequestParam("status") String status,
                                              @RequestParam("reduce") String reduce,
                                              @RequestParam("type") String type,
                                              @RequestParam("membershipDetail") String membershipDetail,
                                              HttpServletResponse servletResponse) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String error = "";
        RestTemplate restTemplate = new RestTemplate();
        boolean stt = false;
        if (status.equals("1")) {
            stt = true;
        }

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/voucher/edit/idVou=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String urlAllVoucher = "http://localhost:8080/api/voucher/getAll";
        ArrayList<VoucherDTO> voucherList = new ArrayList<>();
        Collections.addAll(voucherList, restTemplate.getForEntity(urlAllVoucher, VoucherDTO[].class).getBody());

        VoucherDTO editV = new VoucherDTO();
        for (VoucherDTO v : voucherList) {
            if (v.getId() == id) {
                editV = v;
            }
        }

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        MembershipDetailDTO oldMembershipDetail = new MembershipDetailDTO();
        MembershipDTO oldMembership = new MembershipDTO();
        for (MembershipDTO mem : listAllMembership) {
            for (MembershipDetailDTO memDe: mem.getMembershipDetails()) {
                if(memDe.getId() == Integer.parseInt(membershipDetail)){
                    oldMembershipDetail = memDe;
                    oldMembership = mem;
                    break;
                }
            }
        }

        editV.setVoucherName(voucherName);
        editV.setTimeFrom(inputDateTimeFormat.parse(timeFrom));
        editV.setTimeTo(inputDateTimeFormat.parse(timeTo));
        editV.setStatus(stt);
        editV.setReduce(Integer.parseInt(reduce));
        editV.setType(Integer.parseInt(type));
        editV.setPackageReduce(oldMembershipDetail);

        String urlEditVoucher = "http://localhost:8080/api/voucher/editVoucher";
        HttpEntity<VoucherDTO> requestAddVoucher = new HttpEntity<>(editV);
        ResponseEntity<VoucherDTO> responseAddVoucher = restTemplate.postForEntity(urlEditVoucher, requestAddVoucher, VoucherDTO.class);
        if(responseAddVoucher.getBody().getVoucherCode() != null){
            error += MessageUtil.VALIDATION_VOUCHER_UPDATE_SUCCESS;
        }
        else{
            error += MessageUtil.VALIDATION_VOUCHER_AU_ERR01;
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", error);
        model.addAttribute("listAllMembership", listAllMembership);
        model.addAttribute("editV", editV);
        model.addAttribute("oldMembership", oldMembership);
        model.addAttribute("oldMembershipDetails", oldMembership.getMembershipDetails());
        model.addAttribute("oldMembershipDetail", oldMembershipDetail);
        model.addAttribute("format", format);
        model.addAttribute("inputDateTimeFormat", inputDateTimeFormat);
        return new ModelAndView("admin/editvoucher");
    }

    @RequestMapping("voucher/delete/idVou={id}")
    public ModelAndView adminVoucherDelete(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String error = "";

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/voucher");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        RestTemplate restTemplate = new RestTemplate();
        String urlDeleteVoucher = "http://localhost:8080/api/voucher/deleteVoucher/" + id;
        restTemplate.getForEntity(urlDeleteVoucher, String.class);

        String urlAllVoucher = "http://localhost:8080/api/voucher/getAll";
        ArrayList<VoucherDTO> voucherList = new ArrayList<>();
        Collections.addAll(voucherList, restTemplate.getForEntity(urlAllVoucher, VoucherDTO[].class).getBody());

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("formatter", formatter);
        model.addAttribute("voucherList", voucherList);
        return new ModelAndView("admin/voucher");
    }
}
