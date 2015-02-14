package com.minimocms.type;

import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class MoTextAreaItem extends MoItem {
    String text="";
    String type=Types.textAreaItem;

    protected MoTextAreaItem(){super();}
    public MoTextAreaItem(String name) {
        super(name);
    }


    @Override
    public String text() {
        return text;
    }


    @Override
    public MoTextAreaItem copy() {
        MoTextAreaItem t = new MoTextAreaItem(name());
        t.text=text();
        return t;
    }

    @Override
    public MoTextAreaItem copyWithId() {
        MoTextAreaItem t = copy();
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
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-texteditor-input.vm"));
    }


    @Override
    public String type() {
        return type;
    }

    @Override
    public void setValue(String value) {
        this.text=value;
    }

    @Override
    public String toString(){
        return text();
    }
}
