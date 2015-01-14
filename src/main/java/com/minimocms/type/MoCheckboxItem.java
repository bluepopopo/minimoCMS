package com.minimocms.type;

import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class MoCheckboxItem extends MoItem {
    boolean checked=false;
    String type = Types.checkboxItem;

    public MoCheckboxItem() {
        super();
    }

    public MoCheckboxItem(String name) {
        super(name);
    }


    @Override
    public String text() {
        return ""+checked;
    }

    public boolean checked(){
        return checked;
    }

    @Override
    public MoCheckboxItem copy() {
        MoCheckboxItem t = new MoCheckboxItem(name());
        t.checked = checked();
        return t;
    }

    @Override
    public MoCheckboxItem copyWithId() {
        MoCheckboxItem t = copy();
        t.setId(id());
        return t;
    }

    @Override
    public boolean existsChildById(String id) {
        return false;
    }

    private Map model(String path) {
        Map<String, Object> model = new HashMap<>();
        model.put("label", name());
        model.put("path", path + "/" + id());
        if(checked)model.put("checked", checked());
        return model;
    }

    @Override
    public String render(String path) {
        return Velocity.engine.render(new ModelAndView(model(path), "/assets/minimoassets/vms/render/mo-checkbox-input.vm"));
    }


    @Override
    public String type() {
        return type;
    }

    @Override
    public void setValue(String value) {
        this.checked = value.toLowerCase().equals("true");
    }

    @Override
    public String toString() {
        return text();
    }
}
