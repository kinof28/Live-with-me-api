package com.Abdelwahab.Live.With.ME.services.email;

public interface MailSender {
    void send(String to, String email);
    void send(String to,String subject, String email);

}