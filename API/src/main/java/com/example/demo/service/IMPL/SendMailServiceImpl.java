package com.example.demo.service.IMPL;

import com.example.demo.model.Account;
import com.example.demo.payload.DataMailDTO;
import com.example.demo.service.MailService;
import com.example.demo.service.SendMailService;
import com.example.demo.util.Const;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class SendMailServiceImpl implements SendMailService {
    private final MailService mailService;

    @Override
    public Boolean signup(Account sdi, String token) {
        try {
            DataMailDTO dataMail = new DataMailDTO();

            dataMail.setTo(sdi.getEmail());
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_REGISTER);

            Map<String, Object> props = new HashMap<>();
            props.put("firstname", sdi.getFirstname());
            props.put("lastname", sdi.getLastname());
            props.put("token", "http://localhost:8080/api/auth/accountVerification/" + token);
            dataMail.setProps(props);

            mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CLIENT_REGISTER);
            return true;
        } catch (MessagingException exp) {
            exp.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean create(Account sdi, String password) {
        try {
            DataMailDTO dataMail = new DataMailDTO();

            dataMail.setTo(sdi.getEmail());
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_CREATE_USER);

            Map<String, Object> props = new HashMap<>();
            props.put("firstname", sdi.getFirstname());
            props.put("lastname", sdi.getLastname());
            props.put("username", sdi.getUsername());
            props.put("password", password);
            dataMail.setProps(props);

            mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CLIENT_CREATE_USER);
            return true;
        } catch (MessagingException exp) {
            exp.printStackTrace();
        }
        return false;
    }

    @Override
    public void forgotPassword(Account sdi, String password) {
        try {
            DataMailDTO dataMail = new DataMailDTO();

            dataMail.setTo(sdi.getEmail());
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_FORGET_PASSWORD);

            Map<String, Object> props = new HashMap<>();
            props.put("firstname", sdi.getFirstname());
            props.put("lastname", sdi.getLastname());
            props.put("password", password);
            dataMail.setProps(props);

            mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CLIENT_FORGET_PASSWORD);
        } catch (MessagingException exp) {
            exp.printStackTrace();
        }
    }
}
