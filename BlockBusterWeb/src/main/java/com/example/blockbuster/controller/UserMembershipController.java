package com.example.blockbuster.controller;

import com.example.blockbuster.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class UserMembershipController {
    LoginResponse loginResponse;

    @RequestMapping(method = RequestMethod.GET, path = "user-membership")
    public ModelAndView userMembership(Model model, HttpSession session, HttpServletResponse response) throws IOException {
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if (loginResponse == null) {
            session.setAttribute("oldUrl", "/user-membership");
            response.sendRedirect("/login");
            return null;
        }
        AccountDTO acc = (AccountDTO) session.getAttribute("loginAcc");
        SimpleDateFormat formater = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

        RestTemplate restTemplate = new RestTemplate();
        String urlGetAccountPackage = "http://localhost:8080/api/fkMembership/getAll/" + acc.getUsername();
        ArrayList<FKMembershipDTO> fkMembershipOfAccount = new ArrayList<>();
        Collections.addAll(fkMembershipOfAccount, restTemplate.getForEntity(urlGetAccountPackage, FKMembershipDTO[].class).getBody());

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        Map<MembershipDTO, FKMembershipDTO> fkMembershipOfAccountMap = new TreeMap<>(new Comparator<MembershipDTO>() {
            @Override
            public int compare(MembershipDTO o1, MembershipDTO o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        for (FKMembershipDTO fk: fkMembershipOfAccount) {
            for (MembershipDTO mem: listAllMembership) {
                if(fk.getMembershipId() == mem.getId()){
                    fkMembershipOfAccountMap.put(mem, fk);
                    break;
                }
            }
        }

        model.addAttribute("loginResponse", loginResponse);
        model.addAttribute("fkMembershipOfAccount", fkMembershipOfAccountMap);
        model.addAttribute("packageNum", fkMembershipOfAccount.size());
        model.addAttribute("acc", acc);
        model.addAttribute("formater", formater);
        return new ModelAndView("usermembership");
    }
}
