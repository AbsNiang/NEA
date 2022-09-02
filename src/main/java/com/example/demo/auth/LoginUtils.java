package com.example.demo.auth;
import java.util.regex.Matcher;
public class LoginUtils {
    public static boolean passwordCheck(String password) {
        // at least 1 numb, 1 lower and 1 upper, 1 special char, no whitespace, at least 8 chars, max 45
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,45}";
        return password.matches(pattern);
    }
}
