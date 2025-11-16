package com.azki.reservation.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender sender;
    private final MessageSourceUtil messageSourceUtil;
    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendOtp(String mail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(messageSourceUtil.getMessageIfExist("mail.otp.subject"));
        message.setText(messageSourceUtil.getMessageIfExist("mail.otp.body", otp));
        message.setTo(mail);

        // REMOVE THIS LOG ON PRODUCTION (not even in the debug mode)! Just for the test.
        log.info("otp for {} is {}", mail, otp);

        sender.send(message);
    }
}
