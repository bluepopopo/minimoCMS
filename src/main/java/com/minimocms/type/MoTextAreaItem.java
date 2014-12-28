package com.minimocms.type;

import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class MoTextAreaItem extends MoItem {
    String text="";
    String type=Types.textAreaItem;

    public MoTextAreaItem(){super();}
    public MoTextAreaItem(String name) {
        super(name);
    }
    public MoTextAreaItem(String name, String label) {
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
    public MoTextItem copy() {
        MoTextItem t = new MoTextItem(name(),label());
        t.text=text();
        return t;
    }

    @Override
    public MoTextItem copyWithId() {
        MoTextItem t = new MoTextItem(name(),label());
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
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-textarea-item.vm"));
    }

    @Override
    public String renderMinimal(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-textarea-item-min.vm"));
    }


    @Override
    public String type() {
        return type;
    }

    public void setValue(String value) {
        this.text=value;
    }

}
