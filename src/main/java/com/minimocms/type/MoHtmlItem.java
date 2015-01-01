package com.minimocms.type;

import com.minimocms.utils.Velocity;
import org.apache.commons.lang.StringEscapeUtils;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class MoHtmlItem extends MoItem {
    String text="";
    String type=Types.htmlItem;

    public MoHtmlItem(){super();}
    public MoHtmlItem(String name) {
        super(name);
    }

    @Override
    public String text() {
        return StringEscapeUtils.unescapeHtml(text);
    }

    @Override
    public MoHtmlItem copy() {
        MoHtmlItem t = new MoHtmlItem(name());
        t.text=text();
        return t;
    }

    @Override
    public MoHtmlItem copyWithId() {
        MoHtmlItem t = copy();
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
        model.put("text",text);
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

    @Override
    public void setValue(String value) {
        this.text= StringEscapeUtils.escapeHtml(value);
    }

}
