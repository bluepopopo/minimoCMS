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

    public List<MoId> fileIds();
    public byte[] file(MoId id);
    public MoId saveFile(byte[] bs);

    public boolean setPageFromJson(Request req, String page);
    public boolean setPageFromJson(String page);
}
