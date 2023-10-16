package com.example.demo.controller;

import com.example.demo.dto.MembershipBuyHistoryDTO;
import com.example.demo.model.Membership;
import com.example.demo.service.MembershipBuyHistoryService;
import com.example.demo.service.MembershipDetailService;
import com.example.demo.service.VoucherService;
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
    private final VoucherService voucherService;

    @GetMapping("/getAll")
    public ResponseEntity<List<MembershipBuyHistoryDTO>> getAllBuyHistory() throws Exception {
        return new ResponseEntity<>(membershipBuyHistoryService.getAllBuyHistory(), HttpStatus.OK);
    }

    @GetMapping("/getAll/{username}")
    public ResponseEntity<List<MembershipBuyHistoryDTO>> getAllBuyHistoryByUsername(@PathVariable String username) throws Exception {
        return new ResponseEntity<>(membershipBuyHistoryService.getAllBuyHistoryByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/save/{idAcc}&{idMembershipDetail}")
    public ResponseEntity<String> saveBuyHistory(@PathVariable int idAcc, @PathVariable int idMembershipDetail) throws Exception {
        return new ResponseEntity<>(membershipBuyHistoryService.saveBuyHistory(idAcc, idMembershipDetail, voucherService.getVoucherByMemDeId(idMembershipDetail).getId()), HttpStatus.OK);
    }
}
