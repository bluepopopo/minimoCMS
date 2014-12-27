package com.minimocms.type;

import com.minimocms.utils.Builder;
import com.minimocms.utils.IdUtil;
import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class MoDoc implements GenericContent, Serializable {

    String name;
    String label;
    String _id;
    String type = Types.document;
    Map<String,GenericContent> children = new HashMap<>();

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

    public MoDoc(){}
    public MoDoc(String name) {
        this(name,name);
    }

    public MoDoc(String name, String label) {
        this.name = name;
        this.label = label;
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override

    public Collection<GenericContent> children() {
        return children.values();
    }

    @Override
    public String id() {
        return _id;
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public int nChildren() {
        return children.size();
    }


    private Map model(String path){

        Map<String, Object> model = new HashMap<>();
        model.put("label",label());
        model.put("path",path+"/"+id());
        model.put("children",children());
        return model;
    }

    @Override
    public String render(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/minimo/assets/vms/render/mo-doc.vm"));
    }

    @Override
    public String renderMinimal(String path) {
        return Velocity.engine.render(new ModelAndView(model(path),"/minimo/assets/vms/render/mo-doc-min.vm"));
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

    public void build(Builder<MoDoc> b) {
        b.build(this);
    }

    public <T extends MoItem> void addItem(T i, Builder<T> b) {
        b.build(i);
        children.put(i.name(),i);
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
        MoDoc d = new MoDoc(this.name,this.label);
        children().forEach(c->{
            d.children.put(c.name(),c.copy());
        });
        return d;
    }
}
