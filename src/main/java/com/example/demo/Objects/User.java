package com.example.demo.Objects;

public class User {
    String emailAddress;
    String password;
    String passwordSalt;
    String firstName;
    String surname;
    boolean isAdmin;

    public User(String emailAddress, String password, String passwordSalt, String firstName,
                String surname, boolean isAdmin) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.passwordSalt = passwordSalt;
        this.firstName = firstName;
        this.surname = surname;
        this.isAdmin = isAdmin;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }
}
