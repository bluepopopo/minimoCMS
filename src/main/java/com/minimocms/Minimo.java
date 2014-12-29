package com.minimocms;

import com.minimocms.data.DataStoreInterface;
import com.minimocms.type.GenericContent;
import com.minimocms.type.MoImageItem;
import com.minimocms.type.MoPage;
import com.minimocms.utils.FormUtil;
import com.minimocms.web.Routes;
import spark.Request;
import spark.utils.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            pages().put(page.name(), page);
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

    public static void initClientOnly(String siteName, DataStoreInterface store){
        instance().store = store;
        instance().siteName=siteName;
        instance().rollbackPages();
    }

    public static void save(String name, Request req) {
        MoPage page = page(name);


        FormUtil form = new FormUtil(req);
        form.parseFormInputs();

//        System.out.println("\n\nListing qparams");
//        form.queryParams().forEach(p -> {
//            System.out.println("param:" + p + "=" + form.queryParam(p));
//        });
//        System.out.println("Listing files");
//
//        form.files().forEach(p -> {
//            FileItem f = form.file(p);
//            System.out.println("file:" +p+" "+ f.getFieldName() + " " + f.getContentType() + " " + f.getName());
//        });

        form.queryParams().stream().filter(p->p.startsWith("/")).forEach(p -> {
            String value = form.queryParam(p);
            List<String> ids = new ArrayList<>(Arrays.asList(p.substring(1).split("/")));

            GenericContent c = page.getChildById(ids.get(0));
            ids.remove(0);
            for (String id : ids) {
                if (c.existsChildById(id))
                    c = c.getChildById(id);
                else {
                    throw new IllegalStateException("Something went wrong in creating data, stuck at id:" + c.id());
                }
            }

            c.setValue(value);

        });

        form.queryParams().stream().filter(p->p.startsWith("deleted:")).forEach(p->{
            String value = form.queryParam(p);
            if(value.equals("true")) {
                p = p.substring("deleted:".length());
                List<String> ids = new ArrayList<>(Arrays.asList(p.substring(1).split("/")));
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

        form.queryParams().stream().filter(p->p.startsWith("img:")).forEach(p->{

            List<String> ids = new ArrayList<>(Arrays.asList(p.substring("img:".length()+1).split("/")));

            GenericContent c = page.getChildById(ids.get(0));
            ids.remove(0);
            for (String id : ids) {
                if(c.existsChildById(id))
                    c = c.getChildById(id);
                else{
                    throw new IllegalStateException("Something went wrong in creating data, stuck at id:"+c.id());
                }
            }

            MoImageItem f = (MoImageItem)c;

            if(form.files().contains(p.substring("img:".length()))) {
                try {
                    f.file(IOUtils.toByteArray(form.file(p.substring("img:".length())).getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        persist();
    }


    public static DataStoreInterface store() {
        return instance().store;
    }
}
