package com.example.demo.controller;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.dto.MembershipDetailDTO;
import com.example.demo.model.MembershipDetail;
import com.example.demo.service.MembershipBuyHistoryService;
import com.example.demo.service.MembershipDetailService;
import com.example.demo.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/membership-detail")
public class MembershipDetailController {
    private final MembershipDetailService membershipDetailService;

    @GetMapping("/buy/{idAcc}&{idMembershipDetail}")
    public ResponseEntity<String> buyMembershipDetail(@PathVariable int idAcc, @PathVariable int idMembershipDetail) {
        return new ResponseEntity<>(membershipDetailService.buyMembership(idAcc, idMembershipDetail), HttpStatus.OK);
    }

    @CrossOrigin("*")
    @GetMapping("/getByMembershipId/{id}")
    public ResponseEntity<List<MembershipDetailDTO>> getMembershipDetailByMembershipId(@PathVariable int id) {
        return new ResponseEntity<>(membershipDetailService.getMembershipDetailByMembershipId(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<MembershipDetailDTO> addMembershipDetail(@RequestBody MembershipDetailDTO membershipDetailDTO) throws Exception {
        return new ResponseEntity<>(membershipDetailService.addMembershipDetail(membershipDetailDTO), HttpStatus.OK);
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<String> deleteMembershipDetailById(@PathVariable int id) {
        return new ResponseEntity<>(membershipDetailService.deleteMembershipDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<MembershipDetailDTO> editMembershipDetail(@RequestBody MembershipDetailDTO membershipDetailDTO) {
        return new ResponseEntity<>(membershipDetailService.editMembershipDetail(membershipDetailDTO), HttpStatus.OK);
    }
}
