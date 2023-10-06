package com.example.demo.controller;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.model.MembershipDetail;
import com.example.demo.service.MembershipBuyHistoryService;
import com.example.demo.service.MembershipDetailService;
import com.example.demo.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/membership-detail")
public class MembershipDetailController {
    private final MembershipDetailService membershipDetailService;
    private final MembershipBuyHistoryService membershipBuyHistoryService;

    @GetMapping("/buy/{idAcc}&{idMembershipDetail}")
    public ResponseEntity<String> getMembershipById(@PathVariable int idAcc, @PathVariable int idMembershipDetail) {
        String message = membershipDetailService.buyMembership(idAcc, idMembershipDetail);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
