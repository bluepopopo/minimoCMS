package com.minimocms.data;

import com.minimocms.type.MoPage;
import spark.Request;

import java.util.Map;

public abstract class SimpleDataStoreInterface implements DataStoreInterface{
    @Override
    public Map<String,MoPage> pages(Request req){
        return pages();
    }
    @Override
    public MoPage page(Request req, String name){
        return page(name);
    }
    @Override
    public void rollbackPages(Request req){
        rollbackPages();
    }
    @Override
    public void persistPages(Request req){
        persistPages();
    }
    @Override
    public void persistPage(Request req,MoPage page){
        persistPage(page);
    }
}
