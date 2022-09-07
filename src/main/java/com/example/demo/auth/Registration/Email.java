package com.example.demo.auth.Registration;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    public static void sendEmail(String email, String generatedCode) {

        String sender = "NEA.EmailConfirmation@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.socketFactory.port","587");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable","true");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
           protected PasswordAuthentication getPasswordAuthentication(){
               return new PasswordAuthentication("NEA.EmailConfirmation@gmail.com","smvzdjmkgejryknh"); //App pswrd
           }
        });
        session.setDebug(true);
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("This is the email confirmation code.");
            message.setText(generatedCode);
            Transport.send(message);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

}
