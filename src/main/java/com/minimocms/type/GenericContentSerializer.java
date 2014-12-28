package com.minimocms.type;

import com.google.gson.*;
import com.minimocms.utils.JsonUtil;

import java.lang.reflect.Type;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class GenericContentSerializer implements JsonSerializer<GenericContent>{

    @Override
    public JsonElement serialize(GenericContent genericContent, Type type, JsonSerializationContext jsonSerializationContext) {
        try {
            switch (genericContent.type()) {
                case Types.document:
                    return JsonUtil.gson().toJsonTree(genericContent, MoDoc.class);
                case Types.list:
                    return JsonUtil.gson().toJsonTree(genericContent, MoList.class);
                case Types.textItem:
                    return JsonUtil.gson().toJsonTree(genericContent, MoTextItem.class);
                case Types.fileItem:
                    return JsonUtil.gson().toJsonTree(genericContent, MoFileItem.class);
                case Types.textAreaItem:
                    return JsonUtil.gson().toJsonTree(genericContent, MoTextAreaItem.class);
                default:
                    throw new IllegalStateException("Cannot serialize - " + genericContent.name());
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
