package com.minimocms.type;

import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

import static com.minimocms.Minimo.store;

public class MoImageItem extends MoItem {
    String fileId = "";
    String type = Types.imageItem;
    boolean renderB64 = true;

    public MoImageItem(String name) {
        super(name);
    }

    public MoImageItem() {
        super();
    }

    public String url(){
        return "/minimofile/"+ fileId;
    }

    public byte[] fileBytes(){
        return store().file(fileId);
    }

    public void file(byte[] bytes){
        fileId = store().saveFile(bytes);
    }


    @Override
    public String text() {

        if(renderB64){
            return "data:image/png;base64,"+ DatatypeConverter.printBase64Binary(fileBytes());
        } else {
            return url();
        }
    }



    @Override
    public MoImageItem copy() {
        MoImageItem f = new MoImageItem(name);
        f.fileId=fileId;
        return f;
    }

    @Override
    public MoImageItem copyWithId() {
        MoImageItem f = copy();
        f.setId(id());
        return f;
    }

    @Override
    public boolean existsChildById(String id) {
        return false;
    }

    private Map model(String path) {

        Map<String, Object> model = new HashMap<>();
        model.put("label", name());
        model.put("path", path + "/" + id());
        model.put("fileId", fileId);
        model.put("url", url());

        return model;
    }

    @Override
    public String render(String path) {
        return Velocity.engine.render(new ModelAndView(model(path), "/assets/minimoassets/vms/render/mo-image-input.vm"));
    }

    @Override
    public String type() {
        return type;
    }

    public void setValue(String value) {
        this.fileId = value;
    }

    @Override
    public String toString(){
        return text();
    }
}