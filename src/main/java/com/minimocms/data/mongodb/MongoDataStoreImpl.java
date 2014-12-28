package com.minimocms.data.mongodb;

import com.minimocms.data.Collections;
import com.minimocms.data.DataStoreInterface;
import com.minimocms.data.MoId;
import com.minimocms.type.MoPage;
import com.minimocms.type.MoUser;
import com.minimocms.utils.IdUtil;
import com.minimocms.utils.JsonUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import spark.utils.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class MongoDataStoreImpl implements DataStoreInterface {

    MongoStore store;
    public MongoDataStoreImpl(String dbName){
        dbName="minimo-"+dbName;
        store = new MongoStore(dbName);
    }

    @Override
    public List<MoPage> pages() {
        List<MoPage> pages = new ArrayList<>();
        for(DBObject o:store.collection(Collections.PAGES).find()){
            pages.add(JsonUtil.gson().fromJson(o.toString(), MoPage.class));
        }
        return pages;
    }

    @Override
    public List<MoUser> users() {
        List<MoUser> users = new ArrayList<>();
        for(DBObject o:store.collection(Collections.USERS).find()){
            users.add(JsonUtil.gson().fromJson(o.toString(), MoUser.class));
        }
        return users;
    }

    @Override
    public MoPage page(String name) {
        BasicDBObject doc = new BasicDBObject("name", name);
        return JsonUtil.gson().fromJson(store.collection(Collections.PAGES).findOne(doc).toString(), MoPage.class);
    }

    @Override
    public MoUser user(String username) {
        BasicDBObject doc = new BasicDBObject("username", username);
        return JsonUtil.gson().fromJson(store.collection(Collections.USERS).findOne(doc).toString(), MoUser.class);
    }

    @Override
    public void savePage(MoPage page) {
        store.collection(Collections.PAGES).update(
                new BasicDBObject("name", page.name()),
                (DBObject) JSON.parse(JsonUtil.toJson(page)), true, false);
    }

    @Override
    public void saveUser(MoUser user) {
        store.collection(Collections.USERS).update(
                new BasicDBObject("username", user.getUsername()),
                (DBObject) JSON.parse(JsonUtil.toJson(user)), true, false);
    }

    @Override
    public void savePages(Collection<MoPage> pages) {
        for(MoPage page:pages)
            savePage(page);
    }

    @Override
    public void saveUsers(Collection<MoUser> users) {
        for(MoUser user:users)
            saveUser(user);
    }

    @Override
    public List<MoId> fileIds() {

        List<MoId> ids = new ArrayList<>();

        GridFS grid = store.gridFS(Collections.FILES);
        for(DBObject o: grid.getFileList()){
            ids.add(new MoId(o.get("_id").toString()));
        }
        return ids;
    }

    @Override
    public byte[] file(MoId id) {
        ObjectId oid = new ObjectId(id.getId());
        try {
            return IOUtils.toByteArray(store.gridFS(Collections.FILES).findOne(oid).getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not get file with id:"+id.getId());
        }
    }

    @Override
    public MoId saveFile(byte[] bytes) {
        GridFS grid = store.gridFS(Collections.FILES);
        String md5 = IdUtil.md5(bytes);

        if(grid.findOne(md5)!=null)
            return new MoId((String)grid.findOne(md5).getId().toString());

        GridFSInputFile in = grid.createFile( bytes );
        in.setFilename(md5);
        in.save();

        return new MoId(in.getId().toString());
    }
}
