package com.example.demo.service;

import com.example.demo.payload.DataMailDTO;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;

public interface MailService {
    @Async
    void sendHtmlMail(DataMailDTO dataMail, String templateName) throws MessagingException;
}
