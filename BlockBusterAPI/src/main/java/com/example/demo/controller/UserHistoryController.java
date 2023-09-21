package com.example.demo.controller;

import com.example.demo.dto.UserHistoryDTO;
import com.example.demo.service.UserHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/history")
public class UserHistoryController {
    private final UserHistoryService userHistoryService;
    @GetMapping("/getHistoryBy/{idAcc}/{idMovie}")
    public ResponseEntity<UserHistoryDTO> getHistoryByAccountAndMovie(@PathVariable("idAcc") Integer idAcc, @PathVariable("idMovie") Integer idMovie) throws ParseException {
        return new ResponseEntity<>(userHistoryService.getHistoryByAccountAndMovie(idAcc, idMovie), HttpStatus.OK);
    }
    @GetMapping("/add/{idAcc}/{idMovie}/{movieTime}")
    public ResponseEntity<UserHistoryDTO> createHistory(@PathVariable("idAcc") Integer idAcc, @PathVariable("idMovie") Integer idMovie, @PathVariable("movieTime") String time) throws ParseException {
        return new ResponseEntity<>(userHistoryService.addHistory(idAcc, idMovie, time), HttpStatus.OK);
    }
    @GetMapping("/get-all-by-account/{id}")
    public ResponseEntity<List<UserHistoryDTO>> getAllByAccount(@PathVariable("id") int id){
        return new ResponseEntity<>(userHistoryService.getAllByAccount(id), HttpStatus.OK);
    }
}
