package com.minimocms.type;

import com.minimocms.utils.Builder;
import com.minimocms.utils.IdUtil;
import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
        model.put("label",name());
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
    public void name(String name) {
        this.name=name;
    }


    @Override
    public MoList copy() {
        MoList ls = new MoList(this.name,this.label);
        children().forEach(c->{
            ls.children.add(c.copy());
        });
        ls.itemTemplate=itemTemplate.copy();
        return ls;
    }
    @Override
    public MoList copyWithId() {
        MoList ls = copy();
        ls.setId(id());
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



    @Override
    public <T extends MoItem> void item(T i, Builder<T> b) {
        b.build(i);
        children.add(i);
    }

    @Override
    public <T extends MoItem> void item(T i) {
        children.add(i);
    }


    @Override
    public MoItem item(String name){
        Optional<MoItem> it = items().stream().filter(i->i.name().equals(name)).findFirst();
        if(it.isPresent()==false)return null;
        else return it.get();
    }

    public List<MoDoc> documents(){
        return children()
                .stream()
                .filter(c -> c.type().equals(Types.document))
                .map(c -> (MoDoc) c)
                .collect(Collectors.toList());
    }

    public List<MoList> lists(){
        return children()
                .stream()
                .filter(c -> c.type().equals(Types.list))
                .map(c -> (MoList)c)
                .collect(Collectors.toList());
    }

    @Override
    public List<MoItem> items(){
        return children()
                .stream()
                .filter(c -> c.type().startsWith(Types.item))
                .map(c -> (MoItem)c)
                .collect(Collectors.toList());
    }

    public <T extends GenericContent> MoList  buildTemplate(T c, Builder<T> b) {
        b.build(c);
        itemTemplate = c;
        return this;
    }

    public GenericContent add() {
        GenericContent item = itemTemplate.copy();
        children.add(item);
        return item;
    }

    public List<GenericContent> add(int n) {
        List<GenericContent> ls = new ArrayList<>();
        for(int i = 0; i<n;i++){
            ls.add(add());
        }
        return ls;
    }

    public GenericContent add(Builder<GenericContent> b) {
        GenericContent item = itemTemplate.copy();
        b.build(item);
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
