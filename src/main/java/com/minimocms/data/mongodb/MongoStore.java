package com.minimocms.data.mongodb;

import com.minimocms.utils.JsonUtil;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;

public class MongoStore {

    final static String SERVER="localhost";

    String dbName;

    public MongoStore(String dbName){
        this.dbName=dbName;
    }

    DBCollection collection(String collectionName){
        return db().getCollection(collectionName);
    }
    private DB db(){
        try {
            MongoClient client = new MongoClient(SERVER);
            DB db = client.getDB(dbName);
            return db;
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Unable to connect to MongoDB");
        }
    }

    public GridFS gridFS(String name){
        return new GridFS( db(),name );
    }

    public void insert(String collectionName,Object o) {
        collection(collectionName).insert((DBObject) JSON.parse(JsonUtil.toJson(o)));
    }

}
