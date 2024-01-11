package com.demo.library.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class Mapper {

    public <T, U> U map(T dto, Class<U> objectType)  {
        U object;
        object = getType(objectType);

            Field[] dtoFields = dto.getClass().getDeclaredFields();
            Field[] entityFields = objectType.getDeclaredFields();


            convertDtoToEntity(dto, object, dtoFields, entityFields);

        return object;
    }

     private static <T, U> void convertDtoToEntity(T dto, U object, Field[] dtoFields, Field[] entityFields) {
        for (Field dtoField : dtoFields) {
            for (Field entityField : entityFields) {
                if (isSameField(dtoField, entityField)) {
                    setAccessible(dtoField, entityField);
                    Object value;
                    value = getValueFromDto(dto, dtoField);

                    if (value != null) convertByType(object, entityField, value);
                    break;
                }
            }
        }
    }

    private static <U> void convertByType(U object, Field entityField, Object value) {
        if (isEnumToString(entityField)) {
            enumToString(object, entityField, (String) value);
        }
        else if (isStringToEnum(entityField, value)) {
            stringToEnum(object, entityField, (Enum<?>) value);
        }
        else {
            sameType(object, entityField, value);
        }
    }

    private static <U> void stringToEnum(U object, Field entityField, Enum<?> value) {
        String stringValue = value.toString();
        stringToEnum(object, entityField, stringValue);
    }

    private static <U> void enumToString(U object, Field entityField, String value) {
        Enum<?> enumValue;
        enumValue = convertToEnum(entityField, value);
        enumToString(object, entityField, enumValue);
    }


    private static void setAccessible(Field dtoField, Field entityField) {
        dtoField.setAccessible(true);
        entityField.setAccessible(true);
    }

    private static <U> void sameType(U object, Field entityField, Object value) {
        try {
            entityField.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }

    private static <U> void stringToEnum(U object, Field entityField, String stringValue) {
        try {
            entityField.set(object, stringValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }

    private static boolean isStringToEnum(Field entityField, Object value) {
        return entityField.getType().equals(String.class) && value instanceof Enum;
    }
    private static boolean isEnumToString(Field entityField) {
        return entityField.getType().isEnum();
    }

    private static <U> void enumToString(U object, Field entityField, Enum<?> enumValue) {
        try {
            entityField.set(object, enumValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }

    private static Enum<?> convertToEnum(Field entityField, String value) {
        Enum<?> enumValue;
        try {
            enumValue = Enum.valueOf((Class<Enum>) entityField.getType(), value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to convert value to enum", e);
        }
        return enumValue;
    }

    private static <T> Object getValueFromDto(T dto, Field dtoField) {
        Object value;
        try {
            value = dtoField.get(dto);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get field value", e);
        }
        return value;
    }
    private static boolean isSameField(Field dtoField, Field entityField) {
        return dtoField.getName().equals(entityField.getName());
    }

    private static <U> U getType(Class<U> objectType) {
        U object;
        try {
            object = objectType.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate object", e);
        }
        return object;
    }


}