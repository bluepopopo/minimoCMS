package com.minimocms.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class IdUtil {
    public static String createId(){
        return UUID.randomUUID().toString().substring(0,8);
    }

    public static String md5(byte[] bytes) {
        try {
            MessageDigest d = MessageDigest.getInstance("MD5");
            byte[] array = d.digest(bytes);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return createId();
        }
    }
}
