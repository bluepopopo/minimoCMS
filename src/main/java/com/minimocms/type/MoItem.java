package com.minimocms.type;

import com.minimocms.utils.Builder;
import com.minimocms.utils.IdUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class MoItem implements GenericContent, Serializable {
    String name;
    String _id;

    public MoItem(){}
    protected MoItem(String name) {
        this.name = name;
        _id = IdUtil.createId();
    }
    @Override
    public void name(String name) {
        this.name=name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Collection<GenericContent> children() {
        return new ArrayList<>();
    }

    @Override
    public String id() {
        return _id;
    }
    @Override
    public void setId(String _id) {
        this._id = _id;
    }

    @Override
    public void removeChildById(String id) {
        throw new IllegalArgumentException("Cannot remove child in FileItem:" + id);
    }
    @Override
    public GenericContent getChildById(String id) {
        throw new IllegalArgumentException("Cannot get child in FileItem:" + id);
    }

    @Override
    public String renderMinimal(String path) {
        return render(path);
    }

    public List<MoItem> items(){
        throw new RuntimeException("Cannot access sub-items list from an item");
    }
    public MoItem item(String name){
        throw new RuntimeException("Cannot access sub-item from an item. Item name:"+name()+", Sub-item name:"+name);
    }
    public <T extends MoItem> void item(T i, Builder<T> b){

    }
    public <T extends MoItem> void item(T i){

    }
}
