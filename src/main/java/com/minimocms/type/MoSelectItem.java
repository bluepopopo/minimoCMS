package com.minimocms.type;

import com.minimocms.utils.Pair;
import com.minimocms.utils.Velocity;
import org.apache.commons.lang.StringEscapeUtils;
import spark.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoSelectItem  extends MoItem {
    String option="";
    String type=Types.selectItem;

    List<Pair<String,String>> options = new ArrayList<>();

    public MoSelectItem(){super();}
    public MoSelectItem(String name) {
        super(name);
    }


    @Override
    public String text() {
        return option;
    }


    @Override
    public MoSelectItem copy() {
        MoSelectItem t = new MoSelectItem(name());
        t.option=option;
        t.options=options;
        return t;
    }

    @Override
    public MoSelectItem copyWithId() {
        MoSelectItem t = copy();
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
        model.put("option",option);
        model.put("options",options());

        return model;
    }

    public String option(){
        return option;
    }

    public List<Pair<String, String>> options() {
        return options;
    }

    @Override
    public String render(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-select-item.vm"));
    }

    @Override
    public String renderMinimal(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-select-item-min.vm"));
    }


    @Override
    public String type() {
        return type;
    }

    public void setValue(String value) {
        this.option= StringEscapeUtils.escapeHtml(value);
    }

}

