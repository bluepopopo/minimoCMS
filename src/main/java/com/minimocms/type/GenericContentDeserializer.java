package com.minimocms.type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.minimocms.utils.JsonUtil;

import java.lang.reflect.Type;

public class GenericContentDeserializer implements JsonDeserializer<GenericContent> {
    @Override
    public GenericContent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
//            System.out.println(JsonUtil.toJson(jsonElement.getAsJsonObject()));
            switch (jsonElement.getAsJsonObject().get("type").getAsString()) {
                case Types.document:
                    return JsonUtil.gson().fromJson(jsonElement, MoDoc.class);
                case Types.list:
                    return JsonUtil.gson().fromJson(jsonElement, MoList.class);
                case Types.textItem:
                    return JsonUtil.gson().fromJson(jsonElement, MoTextItem.class);
                case Types.imageItem:
                    return JsonUtil.gson().fromJson(jsonElement, MoImageItem.class);
                case Types.textAreaItem:
                    return JsonUtil.gson().fromJson(jsonElement, MoTextAreaItem.class);
                case Types.selectItem:
                    return JsonUtil.gson().fromJson(jsonElement, MoSelectItem.class);
                case Types.htmlItem:
                    return JsonUtil.gson().fromJson(jsonElement, MoEscapedHtmlItem.class);

                default:
                    throw new IllegalStateException("Cannot deserialize - " + jsonElement.getAsJsonObject().get("type").getAsString());
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
