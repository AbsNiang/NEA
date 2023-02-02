package com.example.demo.Registration;

import java.math.BigInteger;

public class PasswordConverter {

    public static String toHex(byte[] array) {// converts to hex
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public static byte[] fromHex(String hex) { // converts to binary
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16); //parses 2 characters from base 16 to base 2
        }
        return binary;
    }
}
