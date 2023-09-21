package com.example.demo.controller;

import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.AccountPage;
import com.example.demo.dto.Password;
import com.example.demo.exception.MailException;
import com.example.demo.exception.UsernameExitException;
import com.example.demo.model.Account;
import com.example.demo.model.utils.PagingHeaders;
import com.example.demo.model.utils.PagingResponse;
import com.example.demo.service.AccountService;
import com.example.demo.service.ImageService;
import com.example.demo.service.SendMailService;
import com.example.demo.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class ContactController {
    private final ImageService imageService;
    private final SendMailService sendMailService;

    @PostMapping("/contact")
    public void contact(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("content") String content) {
        sendMailService.contact(name, email, content);
        return;
    }
}
