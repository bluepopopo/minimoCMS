package com.minimocms.type;

import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class MoTextItem extends MoItem {
    String text="";
    String type=Types.textItem;

    public MoTextItem(){super();}
    public MoTextItem(String name) {
        super(name);
    }


    @Override
    public String text() {
        return text;
    }


    @Override
    public MoTextItem copy() {
        MoTextItem t = new MoTextItem(name());
        t.text=text();
        return t;
    }

    @Override
    public MoTextItem copyWithId() {
        MoTextItem t = copy();
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
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-text-input.vm"));
    }



    @Override
    public String type() {
        return type;
    }

    @Override
    public void setValue(String value) {
        this.text=value;
    }

}
