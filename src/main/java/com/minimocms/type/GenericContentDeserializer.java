package com.minimocms.type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.minimocms.utils.JsonUtil;

import java.lang.reflect.Type;

/**
 * Created by MattUpstairs on 28/12/2014.
 */
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
