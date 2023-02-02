package com.example.demo.EmailHandling;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email { //add regex

    public static void sendEmail(EmailToken emailToken) {

        String sender = "NEA.EmailConfirmation@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("NEA.EmailConfirmation@gmail.com", "smvzdjmkgejryknh"); //App password
            }
        });
        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailToken.getEmailAddress()));
            message.setSubject(emailToken.getSubject());
            if (emailToken.isSendCode()) {
                message.setText(emailToken.getText() + "\n"+emailToken.getCode());
            } else {
                message.setText(emailToken.getText());
            }
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
