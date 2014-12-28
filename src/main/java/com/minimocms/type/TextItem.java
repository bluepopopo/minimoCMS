package com.minimocms.type;

import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class TextItem extends MoItem {
    String text="";
    String type=Types.textItem;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //    String label="";
    public TextItem(){super();}
    public TextItem(String name) {
        super(name);
    }
    public TextItem(String name,String label) {
        super(name,label);
    }

    public void text(String t){
        text=t;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public String label() {
        return name();
    }



    @Override
    public TextItem copy() {
        TextItem t = new TextItem(name(),label());
        t.text=text();
        return t;
    }
    @Override
    public TextItem copyWithId() {
        TextItem t = new TextItem(name(),label());
        t.text=text();
        t.setId(id());
        return t;
    }

    @Override
    public boolean existsChildById(String id) {
        return false;
    }

    private Map model(String path){

        Map<String, Object> model = new HashMap<>();
        model.put("label",name());
        model.put("path",path+"/"+id());
        model.put("text",text());
        return model;
    }

    @Override
    public String render(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimo/vms/render/mo-text-item.vm"));
    }

    @Override
    public String renderMinimal(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimo/vms/render/mo-text-item-min.vm"));
    }


    @Override
    public String type() {
        return type;
    }


    public GenericContent getOrCreateChildById(String id) {
        throw new IllegalArgumentException("Cannot get child in TextItem:"+id);
    }

    public GenericContent getChildById(String id) {
        throw new IllegalArgumentException("Cannot get child in TextItem:"+id);
    }

    public void setValue(String value) {
        this.text=value;
    }

    public void removeChildById(String id) {
        throw new IllegalArgumentException("Cannot remove child in TextItem:"+id);
    }
}
