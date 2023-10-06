package com.example.demo.controller;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.dto.MovieGenreDTO;
import com.example.demo.service.MembershipService;
import com.example.demo.service.MovieDirectorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/membership")
public class MembershipController {
    private final MembershipService membershipService;

    @GetMapping("/getAll")
    public ResponseEntity<List<MembershipDTO>> getAllMembership() {
        return new ResponseEntity<>(membershipService.getAllMembershipDetail(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipDTO> getMembershipById(@PathVariable int id) {
        return new ResponseEntity<>(membershipService.getMembershipById(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<MembershipDTO> addMembership(@RequestBody MembershipDTO membershipDTO) {
        return new ResponseEntity<>(membershipService.addMembership(membershipDTO), HttpStatus.OK);
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<String> deleteMembershipById(@PathVariable int id) {
        return new ResponseEntity<>(membershipService.deleteMembershipById(id), HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<MembershipDTO> editMembership(@RequestBody MembershipDTO membershipDTO) {
        return new ResponseEntity<>(membershipService.editMembership(membershipDTO), HttpStatus.OK);
    }
}
