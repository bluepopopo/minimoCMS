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

    private Map model(String path){

        Map<String, Object> model = new HashMap<>();
        model.put("label",name());
        model.put("path",path+"/"+id());
        model.put("text",text());
        return model;
    }

    @Override
    public String render(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/minimo/assets/vms/render/mo-text-item.vm"));
    }

    @Override
    public String renderMinimal(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/minimo/assets/vms/render/mo-text-item-min.vm"));
    }


    @Override
    public String type() {
        return type;
    }
}
