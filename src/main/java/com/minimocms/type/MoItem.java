package com.minimocms.type;

import com.minimocms.utils.IdUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public abstract class MoItem implements GenericContent, Serializable {
    String name;
    String label;
    String _id;

    public MoItem(){}
    public MoItem(String name) {
        this(name,name);
    }
    protected MoItem(String name, String label) {
        this.name = name;
        this.label = label;
        _id = IdUtil.createId();
    }
    @Override
    public void name(String name) {
        this.name=name;
    }

    @Override
    public void label(String label) {
        this.label=label;
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
}
