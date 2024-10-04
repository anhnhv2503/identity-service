package com.anhnhvcoder.devteria.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String systemMail;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        // Send email with HTML
        log.info("Sending email from: " + systemMail);
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(systemMail);
        message.setRecipients(Message.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");

        mailSender.send(message);
    }
}
