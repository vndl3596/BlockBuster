package com.example.demo.controller;

import com.example.demo.dto.FKCastDTO;
import com.example.demo.dto.FKMembershipDTO;
import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.model.FKMembership;
import com.example.demo.service.FKCastService;
import com.example.demo.service.FKMembershipService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/fkMembership")
public class FKMembershipController {
    private final FKMembershipService fkMembershipService;

    @GetMapping("/getAll/{username}")
    public ResponseEntity<List<FKMembershipDTO>> getAllCastOnAMovie(@PathVariable String username) {
        return new ResponseEntity<>(fkMembershipService.getFkMembershipByUsername(username), HttpStatus.OK);
    }
}
