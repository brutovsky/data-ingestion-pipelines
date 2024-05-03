package com.nakytniak.mapper;

import com.nakytniak.model.BaseEntityModel;
import com.nakytniak.utils.EntityUtils;

import java.io.Serial;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public final class CsvRowMapper<T extends BaseEntityModel> {

    private final Class<T> entityClass;

    public CsvRowMapper(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T mapRow(final String csvRow) throws Exception {
        final T model = entityClass.getDeclaredConstructor().newInstance();
        final List<Field> entityFields = getNonStaticFields(entityClass);
        final List<String> row = Arrays.asList(csvRow.split(","));

        if (entityFields.size() != row.size()) {
            throw new IllegalArgumentException(String.format("Wrong size of CSV row: %s, should be: %s",
                    row.size(), entityFields.size()));
        }

        int index = 0;
        for (Field field : entityFields) {
            String fieldName = field.getName();
            String fieldValue = row.get(index++);
            Class<?> fieldType = field.getType();
            Object value = convertToFieldType(fieldValue, fieldType);
            EntityUtils.callSetter(model, fieldName, value, fieldType);
        }

        return model;
    }

    private Object convertToFieldType(String value, Class<?> fieldType) {
        if (fieldType == Integer.class || fieldType == int.class) {
            return Integer.parseInt(value);
        } else if (fieldType == Long.class || fieldType == long.class) {
            return Long.parseLong(value);
        } else if (fieldType == Double.class || fieldType == double.class) {
            return Double.parseDouble(value);
        } else if (fieldType == Float.class || fieldType == float.class) {
            return Float.parseFloat(value);
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.parseBoolean(value);
        } else {
            return value;
        }
    }

    private List<Field> getNonStaticFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(field -> !java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                .toList();
    }
}