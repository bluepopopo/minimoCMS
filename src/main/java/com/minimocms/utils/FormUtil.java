package com.minimocms.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import spark.Request;

import java.util.*;

public class FormUtil {

    Request req;
    Map<String, String> inputs = new HashMap<>();
    Map<String, org.apache.commons.fileupload.FileItem> inputFiles = new HashMap<>();

    public FormUtil(Request req){
        this.req=req;
    }
    public boolean isMultipartForm(){
        return ServletFileUpload.isMultipartContent(req.raw());
    }
    public void parseFormInputs() {
        if(isMultipartForm()){
            List<FileItem> items = null;
            try {
                items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req.raw());
            } catch (FileUploadException e) {
                e.printStackTrace();
                return;
            }
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
//                    if(inputs.containsKey(item.getFieldName())==false)
//                        inputs.put(item.getFieldName(), new ArrayList<>());
//                    inputs.get(item.getFieldName()).add(item.getString());
                    inputs.put(item.getFieldName(),item.getString());
                } else {
                    if(item.getName().equals("")==false){
                        inputFiles.put(item.getFieldName(), item);
                    }
                }
            }

        } else {
            req.raw().getParameterMap().forEach((key,value)->{
                inputs.put(key, value[0]);
            });
        }
    }


    public String queryParam(String name){
        return inputs.get(name);
    }

    public Set<String> queryParams(){
        return inputs.keySet();
    }

    public Set<String> files(){
        return inputFiles.keySet();
    }

    public FileItem file(String fieldName){
        return inputFiles.get(fieldName);
    }



}
