package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("smtp.gmail.com")
    private String host;

    @Value("587")
    private Integer port;

    @Value("dangtinnhatroCongNghePhanMem@gmail.com")
    private String email;
//    @abcABC123
    @Value("cfnriaczwzwprutd")
    private String password;

    @Value("true")
    private String isSSL;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(email);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.enable", isSSL);
        props.put("mail.smtp.from", email);
        props.put("mail.debug", "true");

        return mailSender;
    }
}
