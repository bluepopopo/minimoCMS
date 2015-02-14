package com.minimocms.type;

import com.minimocms.utils.*;
import org.apache.commons.lang.StringEscapeUtils;
import spark.ModelAndView;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class MoPage implements Serializable {

    String name;
    String _id;
    String type=Types.page;
    List<GenericContent> children = new ArrayList<>();
    String url = "/";
    boolean copyable=false;

    protected MoPage(){}


    public MoPage(String name) {
        this.name = name;
        _id = IdUtil.createId();
    }

    public boolean isCopyable() {
        return copyable;
    }

    public void setCopyable(boolean copyable) {
        this.copyable = copyable;
    }

    public String url(){
        return url;
    }

    public void url(String url){

        this.url= StringEscapeUtils.escapeHtml(url);
        if(this.url.startsWith("/")==false){
            this.url="/"+this.url;
        }
    }

    public String type() {
        return type;
    }

    public String name() {
        return name;
    }
    public void name(String n){
        this.name=n;
    }

    public Collection<GenericContent> children() {
        return children;
    }

    public String id() {
        return _id;
    }

    public MoDoc document(String docName) {
        if(existsChildByName(docName)){
            if(getChildByName(docName) instanceof MoDoc){
                return (MoDoc)getChildByName(docName);
            } else {
                throw new IllegalArgumentException ("Child - "+docName+" already exists and is not of type MoDoc");
            }
        } else {
            MoDoc doc = new MoDoc(docName);
            children.add(doc);
            return doc;
        }
    }

    public GenericContent item(String name){
        return getChildByName(name);
    }

    public boolean existsChildByName(String docName) {
        for(GenericContent c:children)
            if(c.name().equals(docName))return true;
        return false;
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


    public <T extends MoItem> void addItem(T i, Builder<T> b) {
        b.build(i);
        children.add(i);
    }

    public <T extends MoItem> void addItem(T i) {
        children.add(i);
    }

//    public <T extends GenericContent> MoList list(String listName, Class<T> t) {
//        if(getChildByName(listName)!=null){
//            if(getChildByName(listName) instanceof MoList){
//                return (MoList)getChildByName(listName);
//            } else {
//                throw new IllegalArgumentException ("Child - "+listName+" already exists and is not of type MoList");            }
//        } else {
//            MoList ls = new MoList(listName);
//            children.add(ls);
//            return ls;
//        }
//    }

    private GenericContent getChildByName(String name) {
        for(GenericContent c:children()){
            if(c.name().equals(name))return c;
        }
        return null;
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

    public String render(){
        Map<String, Object> model = new HashMap<>();
        model.put("documents",documents());
        model.put("lists",lists());
        model.put("items",items());
        model.put("url",url());
        model.put("renderUrl",renderUrl());
        model.put("name",name());

        return Velocity.engine.render(new ModelAndView(model, "/assets/minimoassets/vms/render/mo-page.vm"));
    }

    public String renderUrl() {
        return url().replace(":page-name", name());
    }

    public MoPage copy(){

        MoPage page = new MoPage(name);
        page.url(url());
        for(GenericContent c : children)
            page.children.add(c.copy());
        return page;
    }

    public MoPage copyWithId(){
        MoPage page = copy();
        page._id=_id;
        return page;
    }

    @Override
    public String toString(){
        return JsonUtil.toJson(this);
    }

    public String findPath(String listid) {
        Stack<Pair<String,GenericContent>> stack = new Stack<>();
        stack.addAll(this.children().stream().map(
                c -> (Pair<String, GenericContent>) new Pair("/" + c.id(), c))
                .collect(Collectors.toList()));

        while(stack.empty()==false){
            Pair<String,GenericContent> p = stack.pop();
            if(p.second.id().equals(listid))
                return p.first;
            else
                stack.addAll(
                        p.second.children().stream().map(
                                c -> (Pair<String, GenericContent>) new Pair(p.first + "/" + c.id(), c))
                                .collect(Collectors.toList()));
        }
        return null;
    }

    public GenericContent find(String listid) {
        Stack<GenericContent> stack = new Stack<>();
        stack.addAll(this.children());
        while(stack.empty()==false){
            GenericContent c = stack.pop();
            if(c.id().equals(listid))
                return c;
            else
                stack.addAll(c.children());
        }
        return null;
    }

    public GenericContent getOrCreateChildById(String id) {
        if(existsChildById(id))return getChildById(id);
        else throw new IllegalArgumentException("Cannot create child in page:"+id);
    }

    public GenericContent getChildById(String id) {
        for(GenericContent c:children)
            if(c.id().equals(id))return c;
        throw new IllegalArgumentException("Child not found with id:"+id);
    }

    public void setValue(String value) {
        throw new IllegalArgumentException("Should not call setValue() on MoPage");
    }

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

    private boolean existsChildById(String id) {
        for(GenericContent c:children){
            if(c.id().equals(id))return true;
        }
        return false;
    }
}
