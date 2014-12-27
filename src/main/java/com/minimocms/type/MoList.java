package com.minimocms.type;

import com.minimocms.utils.Builder;
import com.minimocms.utils.IdUtil;
import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class MoList  implements GenericContent, Serializable {
    String name;
    String label;
    String _id;
    String type=Types.list;
    Map<String,GenericContent> children = new HashMap<>();
    GenericContent itemTemplate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, GenericContent> getChildren() {
        return children;
    }

    public void setChildren(Map<String, GenericContent> children) {
        this.children = children;
    }

    public GenericContent getItemTemplate() {
        return itemTemplate;
    }

    public void setItemTemplate(GenericContent itemTemplate) {
        this.itemTemplate = itemTemplate;
    }

    public MoList(){}
    public MoList(String name) {
        this(name,name);
    }
    public MoList(String name, String label) {
        this.name = name;
        this.label = label;
        _id = IdUtil.createId();
    }

    @Override
    public String text() {
        return "List - "+name;
    }
    @Override
    public String type() {
        return type;
    }
    @Override
    public String name() {
        return name;
    }

    @Override
    public Collection<GenericContent> children() {
        return children.values();
    }

    @Override
    public String id() {
        return _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    @Override
    public void setId(String _id) {
        this._id = _id;
    }


    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public int nChildren() {
        return children.size();
    }

    private Map<String, Object> model(String path){

        Map<String, Object> model = new HashMap<>();
        model.put("label",label());
        model.put("path",path+"/"+id());

        model.put("id",id());
        model.put("children",children());
        return model;
    }

    @Override
    public String render(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/minimo/assets/vms/render/mo-list.vm"));
    }
    @Override
    public String renderMinimal(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/minimo/assets/vms/render/mo-list-min.vm"));
    }
    @Override
    public String label() {
        return label;
    }

    @Override
    public void name(String name) {
        this.name=name;
    }

    @Override
    public void label(String label) {
        this.label=label;
    }

    @Override
    public MoList copy() {
        MoList ls = new MoList(this.name,this.label);
        children().forEach(c->{
            ls.children.put(c.name(),c.copy());
        });
        return ls;
    }


    public <T extends GenericContent> void  buildTemplate(T c, Builder<T> b) {
        b.build(c);
        itemTemplate = c;
    }

    public GenericContent add() {
        GenericContent item = itemTemplate.copy();
        children.put(item.id(),item);
        return item;
    }
    public GenericContent add(String id) {
        GenericContent item = itemTemplate.copy();
        item.setId(id);
        children.put(item.id(),item);
        return item;
    }
}
