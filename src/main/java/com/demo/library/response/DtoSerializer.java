package com.demo.library.response;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class DtoSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        serializeFields(value, jsonGenerator);
        jsonGenerator.writeEndObject();
    }

    private void serializeFields(Object value, JsonGenerator jsonGenerator) throws IOException {
        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object fieldValue = getFieldValue(value, field);
            if (isNotNull(fieldValue)) {
                if (isNotEmptyList(fieldValue))
                                 serializeListField(field.getName(), fieldValue, jsonGenerator);
                else jsonGenerator.writeObjectField(field.getName(), fieldValue);
            }
        }
    }
    private Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            handleAccessException(e);
            return null;
        }
    }

    private static boolean isNotNull(Object fieldValue) {
        return fieldValue != null;
    }

    private static boolean isNotEmptyList(Object fieldValue) {
        return fieldValue instanceof List;
    }


    private void serializeListField(String fieldName, Object fieldValue, JsonGenerator jsonGenerator) throws IOException {
        List<?> listValue = (List<?>) fieldValue;
        if (isNotEmpty(listValue)) {
            jsonGenerator.writeObjectField(fieldName, fieldValue);
        }
    }

    private static boolean isNotEmpty(List<?> listValue) {
        return !listValue.isEmpty();
    }

    private void handleAccessException(IllegalAccessException e) {
        throw new RuntimeException("Failed to access field", e);
    }
}