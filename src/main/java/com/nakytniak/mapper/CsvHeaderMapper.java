package com.nakytniak.mapper;

import com.nakytniak.model.BaseEntityModel;

import java.io.Serial;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CsvHeaderMapper<T extends BaseEntityModel> {

    private final Class<T> entityClass;

    public CsvHeaderMapper(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public String getHeader(final String csvRow) throws Exception {
        final List<Field> entityFields = getNonStaticFields(entityClass);
        return entityFields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }

    private List<Field> getNonStaticFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(field -> !java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                .toList();
    }
}
