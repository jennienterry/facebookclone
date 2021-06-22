package com.koreait.facebook.common;

import groovy.transform.AutoClone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("luvmin3724@gmail.com"); //보내는사람 이메일
        message.setTo(to); //받는사람 이메일 주소
        message.setSubject(subject); //제목
        message.setText(text); //내용
        emailSender.send(message);
    }
}

