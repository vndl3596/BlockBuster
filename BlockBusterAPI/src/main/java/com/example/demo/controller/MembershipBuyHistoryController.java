package com.example.demo.controller;

import com.example.demo.dto.MembershipBuyHistoryDTO;
import com.example.demo.model.Membership;
import com.example.demo.service.MembershipBuyHistoryService;
import com.example.demo.service.MembershipDetailService;
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
@RequestMapping("/api/membership-buy-his")
public class MembershipBuyHistoryController {
    private final MembershipBuyHistoryService membershipBuyHistoryService;

    @GetMapping("/getAll")
    public ResponseEntity<List<MembershipBuyHistoryDTO>> getAllBuyHistory() {
        return new ResponseEntity<>(membershipBuyHistoryService.getAllBuyHistory(), HttpStatus.OK);
    }

    @GetMapping("/save/{idAcc}&{idMembershipDetail}")
    public ResponseEntity<String> saveBuyHistory(@PathVariable int idAcc, @PathVariable int idMembershipDetail) {
        String message = membershipBuyHistoryService.saveBuyHistory(idAcc, idMembershipDetail, 0);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
