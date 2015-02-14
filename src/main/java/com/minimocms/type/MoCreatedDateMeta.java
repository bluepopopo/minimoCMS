package com.minimocms.type;

import java.io.Serializable;
import java.util.Date;

public class MoCreatedDateMeta extends MoMetaItem implements Serializable {

    Date date;

    final String type=Types.createdDateMeta;

    protected MoCreatedDateMeta(){super();}

    public MoCreatedDateMeta(String name){
        super(name);
        date = new Date();
    }

    @Override
    public String type(){
        return type;
    }

    @Override
    public MoMetaItem copy()  {
        MoMetaItem t = new MoCreatedDateMeta(name());
        return t;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
