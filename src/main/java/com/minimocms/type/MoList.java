package com.minimocms.type;

import com.minimocms.utils.Builder;
import com.minimocms.utils.IdUtil;
import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.io.Serializable;
import java.util.*;

public class MoList  implements GenericContent, Serializable {
    String name;
    String label;
    String _id;
    String type=Types.list;
    List<GenericContent> children = new ArrayList<>();
    GenericContent itemTemplate;

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
        return children;
    }

    @Override
    public String id() {
        return _id;
    }

    @Override
    public void setId(String _id) {
        this._id = _id;
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
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-list.vm"));
    }
    @Override
    public String renderMinimal(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-list-min.vm"));
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
            ls.children.add(c.copy());
        });
        return ls;
    }
    @Override
    public MoList copyWithId() {
        MoList ls = new MoList(this.name,this.label);
        ls.setId(id());
        children().forEach(c->{
            ls.children.add(c.copy());
        });
        return ls;
    }


    public GenericContent get(String name){
        return getChildByName(name);
    }

    private GenericContent getChildByName(String name) {
        for(GenericContent c:children)
            if(c.name().equals(name))
                return c;
        return null;
    }

    @Override
    public GenericContent getChildById(String id) {
        for(GenericContent c:children)
            if(c.id().equals(id))return c;
        throw new IllegalArgumentException("Child not found with id:"+id);
    }

    @Override
    public void setValue(String value) {
        throw new IllegalArgumentException("Should not call setValue() on MoList");
    }



    public <T extends GenericContent> void  buildTemplate(T c, Builder<T> b) {
        b.build(c);
        itemTemplate = c;
    }

    public GenericContent add() {
        GenericContent item = itemTemplate.copy();
        children.add(item);
        return item;
    }
    public GenericContent add(String id) {
        GenericContent item = itemTemplate.copy();
        item.setId(id);
        children.add(item);
        return item;
    }


    @Override
    public void removeChildById(String id) {
        if(existsChildById(id)){
            Iterator<GenericContent> i = children.iterator();
            while(i.hasNext()){
                if(i.next().id().equals(id)){
                    i.remove();
                    return;
                }
            }
        }
    }

    @Override
    public boolean existsChildById(String id) {
        for(GenericContent c:children){
            if(c.id().equals(id))return true;
        }
        return false;
    }
}
