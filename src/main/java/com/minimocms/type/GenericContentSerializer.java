package com.minimocms.type;

import com.google.gson.*;
import com.minimocms.utils.JsonUtil;

import java.lang.reflect.Type;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class GenericContentSerializer implements JsonSerializer<GenericContent>,JsonDeserializer<GenericContent> {

    @Override
    public JsonElement serialize(GenericContent genericContent, Type type, JsonSerializationContext jsonSerializationContext) {
        try {
            switch (genericContent.type()) {
                case Types.document:
                    return JsonUtil.gson().toJsonTree(genericContent, MoDoc.class);
                case Types.list:
                    return JsonUtil.gson().toJsonTree(genericContent, MoList.class);
                case Types.textItem:
                    return JsonUtil.gson().toJsonTree(genericContent, TextItem.class);
                default:
                    throw new IllegalStateException("Cannot serialize - " + genericContent.name());
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

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
                    return JsonUtil.gson().fromJson(jsonElement, TextItem.class);
                default:
                    throw new IllegalStateException("Cannot deserialize - " + jsonElement.getAsJsonObject().get("type").getAsString());
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
