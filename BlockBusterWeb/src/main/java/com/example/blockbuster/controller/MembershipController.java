package com.example.blockbuster.controller;

import com.example.blockbuster.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class MembershipController {

    LoginResponse loginResponse;

    @RequestMapping("membership")
    public ModelAndView viewMembership(Model model,
                                       HttpSession session,
                                       HttpServletResponse response) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/membership");
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        Map<Integer, VoucherDTO> voucherMap = new HashMap<>();

        for (MembershipDTO membershipDTO : listAllMembership) {
            for (MembershipDetailDTO membershipDetailDTO : membershipDTO.getMembershipDetails()) {
                String urlGetVoucher = "http://localhost:8080/api/voucher/getVoucherByMemDeId/" + membershipDetailDTO.getId();
                ResponseEntity<VoucherDTO> responseGetVoucher = restTemplate.getForEntity(urlGetVoucher, VoucherDTO.class);
                VoucherDTO voucherDTO = responseGetVoucher.getBody();
                if (voucherDTO.getVoucherCode() != null) {
                    voucherMap.put(membershipDetailDTO.getId(), voucherDTO);
                }
            }
        }

        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("listAllMembership", listAllMembership);
        model.addAttribute("voucherMap", voucherMap);
        return new ModelAndView("membership");
    }

    @RequestMapping("membership/idMember={id}")
    public ModelAndView viewMembershipById(Model model,
                                           HttpSession session,
                                           HttpServletResponse response,
                                           @PathVariable int id) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/membership/idMember=" + id);
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        MembershipDTO mem = new MembershipDTO();
        for (MembershipDTO memDTO : listAllMembership) {
            if (memDTO.getId() == id) {
                mem = memDTO;
                break;
            }
        }
        Map<Integer, VoucherDTO> voucherMap = new HashMap<>();
        for (MembershipDetailDTO membershipDetailDTO : mem.getMembershipDetails()) {
            String urlGetVoucher = "http://localhost:8080/api/voucher/getVoucherByMemDeId/" + membershipDetailDTO.getId();
            ResponseEntity<VoucherDTO> responseGetVoucher = restTemplate.getForEntity(urlGetVoucher, VoucherDTO.class);
            VoucherDTO voucherDTO = responseGetVoucher.getBody();
            if (voucherDTO.getVoucherCode() != null) {
                voucherMap.put(membershipDetailDTO.getId(), voucherDTO);
            }
        }

        model.addAttribute("mem", mem);
        model.addAttribute("df", df);
        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("listAllMembership", listAllMembership);
        model.addAttribute("voucherMap", voucherMap);
        return new ModelAndView("membership");
    }

    @RequestMapping(method = RequestMethod.GET, path = "membership/buy")
    public ModelAndView buyMembershipGet(Model model,
                                         HttpSession session,
                                         HttpServletResponse response) throws IOException {
        response.sendRedirect("/membership");
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, path = "membership/buy")
    public ModelAndView buyMembershipPost(Model model,
                                          HttpSession session,
                                          HttpServletResponse response,
                                          @RequestParam(value = "inputMemberDetailId", required = true) String buyMemberDetailId) throws IOException {
        DecimalFormat df = new DecimalFormat("#.#");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/membership");
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO loginAcc;
        loginAcc = (AccountDTO) session.getAttribute("loginAcc");

        String urlBuyMembership = "http://localhost:8080/api/membership-detail/buy/" + loginAcc.getId() + "&" + buyMemberDetailId;
        ResponseEntity<String> responseBuyMembership = restTemplate.getForEntity(urlBuyMembership, String.class);

        String urlBuyMembershipHis = "http://localhost:8080/api/membership-buy-his/save/" + loginAcc.getId() + "&" + buyMemberDetailId;
        ResponseEntity<String> responseBuyMembershipHis = restTemplate.getForEntity(urlBuyMembershipHis, String.class);

        Object movieWatching = session.getAttribute("movieWatching");
        if (movieWatching != null) {
            response.sendRedirect("/watch-movie/id=" + (int) movieWatching);
            session.removeAttribute("movieWatching");
            return null;
        }

        response.sendRedirect("/membership");
        return null;
    }
}
