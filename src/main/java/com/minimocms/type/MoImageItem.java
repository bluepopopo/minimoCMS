package com.minimocms.type;

import com.minimocms.data.MoId;
import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static com.minimocms.Minimo.store;

public class MoImageItem extends MoItem {
    String fileId = "";
    String type = Types.imageItem;


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
        return store().file(new MoId(fileId));
    }

    public void file(byte[] bytes){
        fileId = store().saveFile(bytes).getId();
    }


    @Override
    public String text() {
        return fileId;
    }



    @Override
    public MoImageItem copy() {
        MoImageItem f = new MoImageItem(name);
        f.fileId=fileId;
        return f;
    }

    @Override
    public MoImageItem copyWithId() {
        MoImageItem f = new MoImageItem(name);
        f.setId(id());
        f.fileId=fileId;
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
        return Velocity.engine.render(new ModelAndView(model(path), "/assets/minimoassets/vms/render/mo-image-item.vm"));
    }

    @Override
    public String renderMinimal(String path) {
        return Velocity.engine.render(new ModelAndView(model(path), "/assets/minimoassets/vms/render/mo-image-item-min.vm"));
    }


    @Override
    public String type() {
        return type;
    }

    public void setValue(String value) {
        this.fileId = value;
    }

}