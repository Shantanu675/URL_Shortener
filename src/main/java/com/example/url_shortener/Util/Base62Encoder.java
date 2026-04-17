package com.example.url_shortener.Util;

import java.security.SecureRandom;

public class Base62Encoder {

    private static final String CHARSET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final SecureRandom random = new SecureRandom();

    public static String encode(long num){

        StringBuilder sb = new StringBuilder();

        while(num > 0){
            sb.append(CHARSET.charAt((int)(num % 62)));
            num /= 62;
        }

        return sb.reverse().toString();
    }

    // ✅ ADD THIS METHOD
    public static String generateRandomCode(){

        int length = 6; // you can change (6–8 recommended)
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; i++){
            sb.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }

        return sb.toString();
    }
}