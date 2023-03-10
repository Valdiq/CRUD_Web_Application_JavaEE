package com.example.projectpract;

public class Cipher {
    public String cipher(String val) {
        String cipheredVal = "";
        char[] chars = val.toCharArray();
        for (char ch : chars) {
            cipheredVal += (ch += 3);
        }
        return cipheredVal;
    }

    public String deCipher(String val) {
        String deCipheredVal = "";
        char[] chars = val.toCharArray();
        for (char ch : chars) {
            deCipheredVal += (ch -= 3);
        }
        return deCipheredVal;
    }
}
