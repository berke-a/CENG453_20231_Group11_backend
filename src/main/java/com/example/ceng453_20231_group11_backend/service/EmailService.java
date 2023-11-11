package com.example.ceng453_20231_group11_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetToken(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("utku.alparslan@metu.edu.tr");
        message.setTo(to);
        message.setSubject("Password Reset Token");
        message.setText("Here is your password reset token: " + token +
                "\nPlease use this token to reset your password.");

        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Error sending email: ", e);
        }
    }
}