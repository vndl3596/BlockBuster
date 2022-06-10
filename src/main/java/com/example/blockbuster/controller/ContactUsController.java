package com.example.blockbuster.controller;

import com.example.blockbuster.dto.AccountDTO;
import com.example.blockbuster.dto.LoginResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.Properties;

@RestController
@RequestMapping("/")
public class ContactUsController {
    @RequestMapping(method = RequestMethod.GET, path = "contact-us")
    public ModelAndView contactShow(HttpSession session, Model model) {
        LoginResponse loginResponse;
        loginResponse = (LoginResponse) session.getAttribute("loginResponse");
        if(loginResponse != null){
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) session.getAttribute("loginAcc");

            model.addAttribute("loginAcc", loginAcc);
        }

        session.setAttribute("oldUrl", "/contact-us");
        model.addAttribute("loginResponse",loginResponse);
        return new ModelAndView("contactus");
    }

    @RequestMapping(method = RequestMethod.POST, path = "contact-us")
    public ModelAndView contact(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "mail", required = true) String mail,
            @RequestParam(value = "contact-content", required = true) String content,
            HttpSession httpSession,
            Model model
    ) {
        //thêm API đăng ký vào đây
        System.out.println("Bạn đã nhập:");
        System.out.println("Tên: " + name);
        System.out.println("Mail: " + mail);
        System.out.println("Nội dung: " + content);

        String fromMail = "votieubach3596@gmail.com";
        String passWord = "Anhyeuem3596";
        String toMail = "vonguyenduylong92.1415@gmail.com";
        String subject = "Liên hệ từ " + name + " có mail " + mail;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromMail, passWord);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromMail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail, false));
            message.setSubject(subject);
            String htmlContent = "<body><h1>Contact: </h1><h3>Dear Help Desk Service!!! </br> I am " + name + " and my email is " + mail + " </h3><p>" + "Contact Content: " + content + "</p></body>";
            message.setContent(htmlContent, "text/html");

            Transport.send(message);
            System.out.println("Gui mail thành công");
        } catch (MessagingException e) {
            System.out.println("Gửi mail thất bại!!!");
        }

        LoginResponse loginResponse;
        loginResponse = (LoginResponse) httpSession.getAttribute("loginResponse");
        if(loginResponse != null){
            AccountDTO loginAcc;
            loginAcc = (AccountDTO) httpSession.getAttribute("loginAcc");

            model.addAttribute("loginAcc", loginAcc);
        }

        model.addAttribute("loginResponse",loginResponse);
        return new ModelAndView("contactus");
    }
}
