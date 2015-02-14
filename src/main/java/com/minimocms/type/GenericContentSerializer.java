package com.minimocms.type;

import com.google.gson.*;
import com.minimocms.utils.JsonUtil;

import java.lang.reflect.Type;

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
                case Types.imageItem:
                    return JsonUtil.gson().toJsonTree(genericContent, MoImageItem.class);
                case Types.textAreaItem:
                    return JsonUtil.gson().toJsonTree(genericContent, MoTextAreaItem.class);
                case Types.selectItem:
                    return JsonUtil.gson().toJsonTree(genericContent, MoSelectItem.class);
                case Types.htmlItem:
                    return JsonUtil.gson().toJsonTree(genericContent, MoEscapedHtmlItem.class);
                case Types.checkboxItem:
                    return JsonUtil.gson().toJsonTree(genericContent, MoCheckboxItem.class);
                case Types.fileItem:
                    return JsonUtil.gson().toJsonTree(genericContent, MoFileItem.class);

                default:
                    throw new IllegalStateException("Cannot serialize - " + genericContent.name());
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
