package com.minimocms.type;

import com.minimocms.utils.IdUtil;
import com.minimocms.utils.JsonUtil;
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
public class MoPage implements Serializable {

    String name;
    String _id;
    String type=Types.page;
    Map<String,GenericContent> children = new HashMap<>();

    public MoPage(){}


    public MoPage(String name) {
        this.name = name;
        _id = IdUtil.createId();
    }

    public String type() {
        return type;
    }

    public String name() {
        return name;
    }

    public Collection<GenericContent> children() {
        return children.values();
    }

    public String id() {
        return _id;
    }

    public int nChildren() {
        return children.size();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public MoDoc document(String docName) {
        if(children.containsKey(docName)){
            if(children.get(docName) instanceof MoDoc){
                return (MoDoc)children.get(docName);
            } else {
                throw new IllegalArgumentException ("Child - "+docName+" already exists and is not of type MoDoc");
            }
        } else {
            MoDoc doc = new MoDoc(docName);
            children.put(docName,doc);
            return doc;
        }
    }

    public <T extends GenericContent> MoList list(String listName) {
        if(children.containsKey(listName)){
            if(children.get(listName) instanceof MoList){
                return (MoList)children.get(listName);
            } else {
                throw new IllegalArgumentException ("Child - "+listName+" already exists and is not of type MoList");            }
        } else {
            throw new IllegalArgumentException("No list exists - "+listName+" please use list(String listName, Class<T> t) to create");
        }
    }


    public <T extends GenericContent> MoList list(String listName, Class<T> t) {
        if(children.containsKey(listName)){
            if(children.get(listName) instanceof MoList){
                return (MoList)children.get(listName);
            } else {
                throw new IllegalArgumentException ("Child - "+listName+" already exists and is not of type MoList");            }
        } else {
            MoList ls = new MoList(listName);
            children.put(listName,ls);
            return ls;
        }
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

        return Velocity.engine.render(new ModelAndView(model, "/minimo/assets/vms/render/mo-page.vm"));
    }

    @Override
    public String toString(){
        return JsonUtil.toJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
