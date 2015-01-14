package com.minimocms.utils;

import java.io.UnsupportedEncodingException;

public class URLDecoder {

    public static String decodeUTF8(String s){
        try {
            return java.net.URLDecoder.decode(s,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
