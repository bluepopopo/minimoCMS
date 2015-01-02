package com.minimocms.type;

import com.minimocms.utils.Velocity;
import org.apache.commons.lang.StringEscapeUtils;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class MoEscapedHtmlItem extends MoItem {
    String text="";
    String type=Types.htmlItem;

    public MoEscapedHtmlItem(){super();}
    public MoEscapedHtmlItem(String name) {
        super(name);
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public MoEscapedHtmlItem copy() {
        MoEscapedHtmlItem t = new MoEscapedHtmlItem(name());
        t.text=text();
        return t;
    }

    @Override
    public MoEscapedHtmlItem copyWithId() {
        MoEscapedHtmlItem t = copy();
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
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-textarea-input.vm"));
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
