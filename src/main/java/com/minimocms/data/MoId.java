package com.minimocms.data;

import org.bson.types.ObjectId;

public class MoId {
    String id;

    public MoId(String id) {
        if(id.length()!=24)throw new IllegalArgumentException("Error, id isn't 24 bytes:"+id);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return id;
    }

    public ObjectId toObjectId() {
        return new ObjectId();
    }
}
