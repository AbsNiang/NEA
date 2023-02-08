package com.example.demo.EmailHandling;

import javafx.scene.control.Alert;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//email app password expires after certain amount of time
public class Email {//neaprojectemailsend@gmail.com, normal password is NEAP4ssword!, app password is gabdgvnjigejtkzz

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
                //return new PasswordAuthentication("NEA.EmailConfirmation@gmail.com", "smvzdjmkgejryknh"); //alternative sender email
                return new PasswordAuthentication("neaprojectemailsend@gmail.com", "gabdgvnjigejtkzz"); //App password
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This email doesn't exist.");
            alert.show();
        }
    }

}
