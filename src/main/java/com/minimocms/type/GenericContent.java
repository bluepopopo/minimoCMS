package com.minimocms.type;

import java.util.Collection;

public interface GenericContent {
    public String type();
    public String name();
    public String text();
    public Collection<GenericContent> children();
    public String id();
    public void setId(String id);
    public String render(String path);
    public String renderMinimal(String path);
    public String label();
    public void name(String name);
    public void label(String label);
    public GenericContent copy();
    public GenericContent copyWithId();

    public boolean existsChildById(String id);
    public GenericContent getChildById(String id);

    public void setValue(String value);

    public void removeChildById(String toDelete);
}
