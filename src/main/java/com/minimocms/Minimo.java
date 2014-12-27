package com.minimocms;

import com.minimocms.data.DataStoreInterface;
import com.minimocms.type.MoPage;
import com.minimocms.web.Routes;
import spark.Request;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class Minimo {

    DataStoreInterface store;
    String siteName;
    private Map<String,MoPage> pages = new HashMap<>();

    static Minimo minimo = new Minimo();
    public static Minimo instance(){
        return minimo;
    }


    public static MoPage page(String name){
        if(instance().pages.containsKey(name)){
            return instance().pages.get(name);
        } else {
            MoPage page = new MoPage(name);
            instance().pages.put(name,page);
            return page;
        }
    }

    public static Collection<MoPage> pages(){
        return instance().pages.values();
    }
//
//    public static void init(String siteName){
//        init(siteName,new MongoStore(siteName));
//    }

    public static void init(String siteName, DataStoreInterface store){
        instance().store = store;
        instance().siteName=siteName;
        new Routes().init();
    }

    public static void save(String name, Request req) {
        MoPage page = page(name);



    }
}
