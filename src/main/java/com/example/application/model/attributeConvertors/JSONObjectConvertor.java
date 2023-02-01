package com.example.application.model.attributeConvertors;

import org.json.JSONObject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class JSONObjectConvertor implements AttributeConverter<JSONObject, String> {
    @Override
    public String convertToDatabaseColumn(JSONObject jsonObject) {
        if (Objects.nonNull(jsonObject)) return jsonObject.toString();
        return "";
    }

    @Override
    public JSONObject convertToEntityAttribute(String jsonObject) {
        if (jsonObject.isEmpty()) return null;
        return new JSONObject(jsonObject);
    }
}
