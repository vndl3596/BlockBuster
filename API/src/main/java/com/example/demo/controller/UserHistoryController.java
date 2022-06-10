package com.example.demo.controller;

import com.example.demo.DTO.UserHistoryDTO;
import com.example.demo.service.UserHistoryService;
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
@RequestMapping("/api/history")
public class UserHistoryController {
    private final UserHistoryService userHistoryService;
    @GetMapping("/add-history/{idAcc}/{idMovie}")
    public ResponseEntity<UserHistoryDTO> addHistory(@PathVariable("idAcc") Integer idAcc, @PathVariable("idMovie") Integer idMovie) {
        return new ResponseEntity<>(userHistoryService.addHistory(idAcc, idMovie), HttpStatus.OK);
    }
    @GetMapping("/get-all-by-account/{id}")
    public ResponseEntity<List<UserHistoryDTO>> getAllByAccount(@PathVariable("id") int id){
        return new ResponseEntity<>(userHistoryService.getAllByAccount(id), HttpStatus.OK);
    }
}
