package com.minimocms.data;

import com.minimocms.type.MoPage;
import com.minimocms.type.MoUser;
import spark.Request;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DataStoreInterface {

    public Map<String,MoPage> pages();
    public MoPage page(String name);
    public boolean setPagesFromJson(String json);
    public void rollbackPages();

    public Map<String,MoPage> pages(Request req);
    public MoPage page(Request req, String name);
    public void rollbackPages(Request req);
    public void persistPages(Request req);
    public void persistPage(Request req,MoPage page);
    public boolean setPagesFromJson(Request req,String json);


    public List<MoUser> users();
    public MoUser user(String username);

    public void persistPages();
    public void persistPage(MoPage page);
    public void persistUser(MoUser user);
    public void persistUsers(Collection<MoUser> users);

    public List<String> fileIds();
    public byte[] file(String id);
    public String saveFile(byte[] bs);
    public String saveFile(byte[] bytes, String fileName);

    public boolean setPageFromJson(Request req, String page);
    public boolean setPageFromJson(String page);

    public boolean existsPage(String name);
    public boolean existsPage(Request req, String name);

    public boolean deletePage(String name);
    public boolean deletePage(Request req,String name);

}
