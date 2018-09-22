package br.cefetmg.inf.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Eduardo Cotta
 */
public class PasswordCriptography {
    
    public static String generateMd5(String password) {
        String pass = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(password.getBytes()));
        pass = hash.toString(16);
        return pass;
    }
}
