package com.example.demo.controller;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.service.MembershipService;
import com.example.demo.service.MovieDirectorService;
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
}
