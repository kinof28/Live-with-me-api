package com.Abdelwahab.Live.With.ME.services.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class MailService implements MailSender {
    private final JavaMailSender mailSender;// = new JavaMailSenderImpl();

    @Override
    @Async
    public void send(String to, String email) {
        //mailSender.setHost("localhost");
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(email, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("Confirm your email in Live With ME");
            mimeMessageHelper.setFrom("Abdo@Hamadouche.mascara");
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Some shit went wrong here -- : " + e);
        }
    }
    @Override
    @Async
    public void send(String to,String subject, String email) {
        //mailSender.setHost("localhost");
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(email, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("Abdo@Hamadouche.mascara");
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Some shit went wrong here -- : " + e);
        }
    }

}