package com.example.rentaperson.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class SendEmailService {

    private final JavaMailSender javaMailSender;

    public SendEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String toEmail,String subject,String body){

        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("shabora1419@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
