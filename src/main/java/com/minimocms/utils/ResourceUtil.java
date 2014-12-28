package com.minimocms.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {
    public ResourceUtil(){}
    public InputStream getFileStream(String file){
        return getClass().getResourceAsStream(file);
    }
    public byte[] getFileBytes(String file) {
        try{
            return IOUtils.toByteArray(getFileStream(file));
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
