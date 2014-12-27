package com.minimocms.type;

import com.minimocms.utils.IdUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public abstract class MoItem implements GenericContent, Serializable {
    String name;
    String label;
    String _id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

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
    public boolean hasChildren() {
        return false;
    }

    @Override
    public int nChildren() {
        throw new UnsupportedOperationException("Cannot call nChildren() on 'Item' type");
    }
}
