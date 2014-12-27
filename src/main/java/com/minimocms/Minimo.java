package com.minimocms;

import com.minimocms.data.DataStoreInterface;
import com.minimocms.type.GenericContent;
import com.minimocms.type.MoPage;
import com.minimocms.web.Routes;
import spark.Request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class Minimo {

    public DataStoreInterface store;
    String siteName;

    Map<String,MoPage> pages;

    static Minimo minimo = new Minimo();
    public static Minimo instance(){
        return minimo;
    }

    public static MoPage page(String name){
        if(pages().containsKey(name)){
            return pages().get(name);
        } else {
            MoPage page = new MoPage(name);
            store().savePage(page);
            rollbackPages();
            return page;
        }
    }

    public static void persist(){
        store().savePages(pages().values());
    }

    public static Map<String,MoPage> pages(){
        return instance().pages;
    }

    public static void rollbackPages(){
        instance().pages = store().pages().stream().collect(Collectors.toMap(p->p.name(),p->p));
    }
//
//    public static void init(String siteName){
//        init(siteName,new MongoStore(siteName));
//    }

    public static void init(String siteName, DataStoreInterface store){
        instance().store = store;
        instance().siteName=siteName;
        instance().rollbackPages();
        new Routes().init();
    }

    public static void save(String name, Request req) {
        MoPage page = page(name);

        req.queryParams().stream().filter(p->p.startsWith("deleted:")==false).forEach(p->{
            String value = req.queryParams(p);
            List<String> ids = Arrays.asList(p.substring(1).split("/"));

            GenericContent c=page.getChildById(ids.get(0));
            ids.remove(0);
            for(String id:ids){
                 c = c.getOrCreateChildById(id);
            }

            c.setValue(value);
        });

        req.queryParams().stream().filter(p->p.startsWith("deleted:")==false).forEach(p->{
            String value = req.queryParams(p);
            if(value.equals("true")) {
                p = p.substring("deleted:".length());
                List<String> ids = Arrays.asList(p.substring(1).split("/"));
                GenericContent c = page.getChildById(ids.get(0));
                ids.remove(0);
                String toDelete = ids.get(ids.size()-1);
                ids.remove(ids.size()-1);
                for (String id : ids) {
                    c = c.getChildById(id);
                }
                c.removeChildById(toDelete);
            }
        });
    }


    public static DataStoreInterface store() {
        return instance().store;
    }
}
