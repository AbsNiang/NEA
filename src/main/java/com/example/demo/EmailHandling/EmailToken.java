package com.example.demo.EmailHandling;

public class EmailToken {
    String emailAddress;
    String code;
    String subject;
    String text;
    boolean sendCode;

    public EmailToken(String emailAddress, String code, String subject, String text, boolean sendCode) {
        this.emailAddress = emailAddress;
        this.code = code;
        this.subject = subject;
        this.text = text;
        this.sendCode = sendCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSendCode() {
        return sendCode;
    }

    public void setSendCode(boolean sendCode) {
        this.sendCode = sendCode;
    }
}
