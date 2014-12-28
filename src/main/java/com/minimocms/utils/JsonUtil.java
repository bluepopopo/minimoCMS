package com.minimocms.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minimocms.type.GenericContent;
import com.minimocms.type.GenericContentDeserializer;
import com.minimocms.type.GenericContentSerializer;

public class JsonUtil {
    static Gson gson;
    static {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(GenericContent.class,new GenericContentSerializer());
        b.registerTypeAdapter(GenericContent.class,new GenericContentDeserializer());

        gson = b.create();
    }

    public static String toJson(Object o) {
        return gson.toJson(o);
    }


    public static Gson gson(){
        return gson;
    }

    public static void println(Object o) {
        System.out.println(toJson(o));
    }
}
