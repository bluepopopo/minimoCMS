package com.minimocms.data.mongodb;

import com.google.gson.reflect.TypeToken;
import com.minimocms.data.Collections;
import com.minimocms.data.SimpleDataStoreInterface;
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

import java.util.*;
import java.util.stream.Collectors;

public class MongoDataStoreImpl extends SimpleDataStoreInterface {

    Map<String,MoPage> pages = new HashMap<>();

    String dbName;

    public MongoDataStoreImpl(String dbName){
        this.dbName="minimo-"+dbName;
    }

    @Override
    public Map<String,MoPage> pages() {
        return pages;
    }

    @Override
    public void rollbackPages(){
        MongoStore store = new MongoStore(this.dbName);
        List<MoPage> ps = new ArrayList<>();
        for(DBObject o:store.collection(Collections.PAGES).find()){
            ps.add(JsonUtil.gson().fromJson(o.toString(), MoPage.class));
        }
        pages= ps.stream().collect(Collectors.toMap(p->p.name(),p->p));
        store.close();
    }

    @Override
    public boolean deletePage(String name){
        MongoStore store = new MongoStore(this.dbName);
        BasicDBObject page = new BasicDBObject();
        page.put("name", name);
        WriteResult res = store.collection(Collections.PAGES).remove(page);
        store.close();
        return res.getN()==1;
    }

    @Override
    public List<MoUser> users() {
        MongoStore store = new MongoStore(this.dbName);
        List<MoUser> users = new ArrayList<>();
        for(DBObject o:store.collection(Collections.USERS).find()){
            users.add(JsonUtil.gson().fromJson(o.toString(), MoUser.class));
        }
        store.close();
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
        MongoStore store = new MongoStore(this.dbName);
        BasicDBObject doc = new BasicDBObject("username", username);
        MoUser user = JsonUtil.gson().fromJson(store.collection(Collections.USERS).findOne(doc).toString(), MoUser.class);
        store.close();
        return user;
    }

    @Override
    public void persistPage(MoPage page) {
        MongoStore store = new MongoStore(this.dbName);
        store.collection(Collections.PAGES).update(
                new BasicDBObject("name", page.name()),
                (DBObject) JSON.parse(JsonUtil.toJson(page)), true, false);
        store.close();
    }

    @Override
    public void persistUser(MoUser user) {
        MongoStore store = new MongoStore(this.dbName);
        store.collection(Collections.USERS).update(
                new BasicDBObject("username", user.getUsername()),
                (DBObject) JSON.parse(JsonUtil.toJson(user)), true, false);
        store.close();
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
        MongoStore store = new MongoStore(this.dbName);

        List<String> ids = new ArrayList<>();

        GridFS grid = store.gridFS(Collections.FILES);
        for(DBObject o: grid.getFileList()){
            ids.add(o.get("filename").toString());
        }

        store.close();
        return ids;
    }

    @Override
    public byte[] file(String filename) {

        MongoStore store = new MongoStore(this.dbName);
        byte[] bs=null;
        try {
            return IOUtils.toByteArray(store.gridFS(Collections.FILES).findOne(filename).getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
//            throw new IllegalArgumentException("Could not get file with filename:"+filename);
        } finally {
            store.close();
        }
        return new byte[0];
    }

    @Override
    public String saveFile(byte[] bytes) {
        MongoStore store = new MongoStore(this.dbName);
        GridFS grid = store.gridFS(Collections.FILES);
        String md5 = IdUtil.md5(bytes);

        if(grid.findOne(md5)!=null)
            return grid.findOne(md5).getFilename();

        GridFSInputFile in = grid.createFile( bytes );
        in.setFilename(md5);
        in.save();


        String filename= in.getFilename();
        store.close();
        return filename;
    }

    @Override
    public String saveFile(byte[] bytes, String fileName) {
        MongoStore store = new MongoStore(this.dbName);
        GridFS grid = store.gridFS(Collections.FILES);

        if(grid.findOne(fileName)!=null)
            return grid.findOne(fileName).getFilename();

        GridFSInputFile in = grid.createFile( bytes );
        in.setFilename(fileName);
        in.save();


        String filename= in.getFilename();
        store.close();
        return filename;
    }
}
