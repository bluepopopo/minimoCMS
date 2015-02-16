package com.minimocms.data.mysql;

import com.google.gson.reflect.TypeToken;
import com.minimocms.data.Collections;
import com.minimocms.data.SimpleDataStoreInterface;
import com.minimocms.data.mongodb.MongoStore;
import com.minimocms.type.MoPage;
import com.minimocms.type.MoUser;
import com.minimocms.utils.IdUtil;
import com.minimocms.utils.JsonUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;
import spark.utils.IOUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Matt on 16/02/2015.
 */
public class MysqlDataStoreImpl extends SimpleDataStoreInterface {


    Map<String,MoPage> pages = new HashMap<>();

    String dbName;
    MysqlStore store;

    public MysqlDataStoreImpl(String dbName){
        this.dbName="minimo"+dbName;
        store = new MysqlStore(this.dbName);
    }

    @Override
    public Map<String,MoPage> pages() {
        return pages;
    }

    @Override
    public void rollbackPages(){

        List<MoPage> ps = new ArrayList<>();
        for(String name:store.names(Collections.PAGES)){
            ps.add(JsonUtil.gson().fromJson(store.select(Collections.PAGES,name), MoPage.class));
        }
        pages= ps.stream().collect(Collectors.toMap(p -> p.name(), p -> p));
    }

    @Override
    public boolean deletePage(String name){
        store.delete(Collections.PAGES, name);
        return true;
    }

    @Override
    public List<MoUser> users() {
        List<MoUser> users = new ArrayList<>();
        for(String name:store.names(Collections.USERS)){
            users.add(JsonUtil.gson().fromJson(store.select(Collections.USERS, name), MoUser.class));
        }
        return users;
    }

//    @Override
//    public MoPage page(String name) {
//        BasicDBObject doc = new BasicDBObject("name", name);
//        return JsonUtil.gson().fromJson(store.collection(Collections.PAGES).findOne(doc).toString(), MoPage.class);
//    }

    @Override
    public MoPage page(String name){
        if(pages().containsKey(name)){
            return pages().get(name);
        } else {
            MoPage page = new MoPage(name);
            pages().put(page.name(), page);
            return page;
        }
    }

    @Override
    public boolean existsPage(String name){
        return pages().containsKey(name);
    }

    @Override
    public boolean setPagesFromJson(String json) {
        try{
            List<MoPage> pages = JsonUtil.gson().fromJson(json, new TypeToken<ArrayList<MoPage>>() {}.getType());
            this.pages = pages.stream().collect(Collectors.toMap(p->p.name(),p->p));
        } catch (Exception e){
            System.err.println("Could not deserialize json: "+json);
            return false;
        }
        return true;
    }

    @Override
    public boolean setPageFromJson(String json) {
        try{
            MoPage page = JsonUtil.gson().fromJson(json, MoPage.class);
            this.pages().put(page.name(),page);
        } catch (Exception e){
            System.err.println("Could not deserialize json: "+json);
            return false;
        }
        return true;
    }

    @Override
    public MoUser user(String username) {
        return  JsonUtil.gson().fromJson(store.select(Collections.USERS, username), MoUser.class);

    }

    @Override
    public void persistPage(MoPage page) {
        store.insertOrUpdate(Collections.PAGES,page.name(),JsonUtil.toJson(page));
    }

    @Override
    public void persistUser(MoUser user) {
        store.insertOrUpdate(Collections.PAGES, user.getUsername(), JsonUtil.toJson(user));
    }

    @Override
    public void persistPages(){
        for(MoPage page:pages.values())
            persistPage(page);
    }
    //    @Override
//    public void savePages(Collection<MoPage> pages) {
//        for(MoPage page:pages)
//            savePage(page);
//    }
//
    @Override
    public void persistUsers(Collection<MoUser> users) {
        for(MoUser user:users)
            persistUser(user);
    }

    @Override
    public List<String> fileIds() {
        return new ArrayList<>(store.names(Collections.FILES));
    }

    @Override
    public byte[] file(String filename) {

        return store.select(Collections.FILES,filename).getBytes();
    }

    @Override
    public String saveFile(byte[] bytes) {
        String md5 = IdUtil.md5(bytes);

        if(fileIds().stream().anyMatch(id->id.equals(md5))){
            return md5;
        }

        store.insertOrUpdate(Collections.FILES, md5, new String(bytes));

        return md5;
    }

    @Override
    public String saveFile(byte[] bytes, String fileName) {
        String md5 = fileName;

        if(fileIds().stream().anyMatch(id->id.equals(md5))){
            return md5;
        }

        store.insertOrUpdate(Collections.FILES,md5,new String(bytes));

        return md5;
    }

}
