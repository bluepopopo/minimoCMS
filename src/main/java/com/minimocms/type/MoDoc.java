package com.minimocms.type;

import com.minimocms.utils.Builder;
import com.minimocms.utils.IdUtil;
import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class MoDoc implements GenericContent, Serializable {

    String name;
    String _id;
    String type = Types.document;
    List<GenericContent> children = new ArrayList<>();

    public MoDoc(){}
    public MoDoc(String name) {
        this.name = name;
        _id = IdUtil.createId();
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
    public String text() {
        return "Document - "+name;
    }

    @Override
    public Collection<GenericContent> children() {
        return children;
    }

    @Override
    public String id() {
        return _id;
    }

    public <T extends GenericContent> MoList list(String listName) {
        if(getChildByName(listName)!=null){
            if(getChildByName(listName) instanceof MoList){
                return (MoList)getChildByName(listName);
            } else {
                throw new IllegalArgumentException ("Child - "+listName+" already exists and is not of type MoList");            }
        } else {
//            throw new IllegalArgumentException("No list exists - "+listName+" please use list(String listName, Class<T> t) to create");
            MoList ls = new MoList(listName);
            children.add(ls);
            return ls;
        }
    }
    private Map model(String path){

        Map<String, Object> model = new HashMap<>();
        model.put("label",name());
        model.put("id",id());
        model.put("path",path+"/"+id());
        model.put("children",children());
        return model;
    }

    @Override
    public String render(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-doc.vm"));
    }

    @Override
    public String renderMinimal(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/assets/minimoassets/vms/render/mo-doc-min.vm"));
    }

    @Override
    public void name(String name) {
        this.name=name;
    }
    @Override
    public void setId(String _id) {
        this._id = _id;
    }


    public void build(Builder<MoDoc> b) {
        b.build(this);
    }

    public <T extends MoItem> void item(T i, Builder<T> b) {
        b.build(i);
        children.add(i);
    }

    public <T extends MoItem> void item(T i) {
        children.add(i);
    }


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

    public List<MoItem> items(){
        return children()
                .stream()
                .filter(c -> c.type().startsWith(Types.item))
                .map(c -> (MoItem)c)
                .collect(Collectors.toList());
    }

    public MoDoc copy(){
        MoDoc d = new MoDoc(this.name);
        children().forEach(c->{
            d.children.add(c.copy());
        });
        return d;
    }
    public MoDoc copyWithId(){
        MoDoc d = copy();
        d.setId(id());
        return d;
    }

    @Override
    public GenericContent getChildById(String id) {
        for(GenericContent c:children)
            if(c.id().equals(id))return c;
        throw new IllegalArgumentException("Child not found with id:"+id);
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
    public void setValue(String value) {
        throw new IllegalArgumentException("Should not call setValue() on MoDoc");
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
