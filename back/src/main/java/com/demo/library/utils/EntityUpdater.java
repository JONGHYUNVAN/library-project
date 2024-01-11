package com.demo.library.utils;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;
@Component
@Setter
@RequiredArgsConstructor
public class EntityUpdater<T> {

    public void update(T entity, T verifiedEntity, Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            verifyAndUpdateField(entity, verifiedEntity, field);
        }
    }

    private void verifyAndUpdateField(T entity, T verifiedEntity, Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(entity);

            if (isNotNull(value)) {
                Field verifiedField = verifyAndGetField(verifiedEntity, field);
                verifiedField.setAccessible(true);

                Consumer<Object> fieldSetter = verifyAndGetFieldSetter(verifiedEntity, verifiedField);
                updateField(value, fieldSetter);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error at verifyAndUpdateField",e);
        }
    }

    private static boolean isNotNull(Object value) {
        return value != null;
    }

    private static Field verifyAndGetField(Object verifiedEntity, Field field) throws NoSuchFieldException {
        return verifiedEntity.getClass().getDeclaredField(field.getName());
    }

    private static Consumer<Object> verifyAndGetFieldSetter(Object verifiedEntity, Field verifiedField) throws NoSuchMethodException {
        String setterMethodName = "set" + capitalizeFirstLetter(verifiedField.getName());
        Method setterMethod = verifiedEntity.getClass().getMethod(setterMethodName, verifiedField.getType());

        setterMethod.setAccessible(true);

        return getSetterConsumer(verifiedEntity, setterMethod);
    }

    private static Consumer<Object> getSetterConsumer(Object verifiedEntity, Method setterMethod) {
        return (obj) -> {
            try {
                setterMethod.invoke(verifiedEntity, obj);
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Unexpected error at getSetterConsumer",e);
            }
        };
    }

    private static String capitalizeFirstLetter(String str) {
        if (isNotBlankOrNull(str)) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private static boolean isNotBlankOrNull(String str) {
        return str == null || str.isEmpty();
    }

    private void updateField(Object value, Consumer<Object> setter) {
        Optional.ofNullable(value).ifPresent(setter);
    }
}


