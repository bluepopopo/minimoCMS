package com.minimocms.type;

import com.minimocms.utils.Velocity;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static com.minimocms.Minimo.store;

public class MoFileItem extends MoItem {
    String fileId = "";
    String type = Types.fileItem;

    public MoFileItem(String name) {
        super(name);
    }

    protected MoFileItem() {
        super();
    }

    public String url() {
        return "/minimofile/" + fileId;
    }

    public byte[] fileBytes() {
        return store().file(fileId);
    }

    public void file(byte[] bytes, String fileName) {
//        try {
//            if(Minimo.maxFileSize>=0){
//                bytes=ImgUtil.resizeImageAsJPG(bytes, Minimo.maxFileSize);
//            }
            fileId = store().saveFile(bytes,fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String text() {
        return url();
    }


    @Override
    public MoFileItem copy() {
        MoFileItem f = new MoFileItem(name);
        f.fileId = fileId;
        return f;
    }

    @Override
    public MoFileItem copyWithId() {
        MoFileItem f = copy();
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
        return Velocity.engine.render(new ModelAndView(model(path), "/assets/minimoassets/vms/render/mo-file-input.vm"));
    }

    @Override
    public String type() {
        return type;
    }

    public void setValue(String value) {
        this.fileId = value;
    }

    @Override
    public String toString() {
        return text();
    }
}
