package com.minimocms.type;

import com.minimocms.utils.Builder;

import java.util.Collection;
import java.util.List;

public interface GenericContent {
    public String type();
    public String name();
    public String text();
    public Collection<GenericContent> children();
    public String id();
    public void setId(String id);
    public String render(String path);
    public String renderMinimal(String path);
    public void name(String name);
    public GenericContent copy();
    public GenericContent copyWithId();

    public List<MoItem> items();
    public MoItem item(String name);
    public <T extends MoItem> void item(T i, Builder<T> b);
    public <T extends MoItem> void item(T i);


    public boolean existsChildById(String id);
    public GenericContent getChildById(String id);

    public void setValue(String value);

    public void removeChildById(String toDelete);
}
