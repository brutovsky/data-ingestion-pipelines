package com.nakytniak.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.beam.repackaged.core.org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityUtils {

    private static final Logger log = LoggerFactory.getLogger(EntityUtils.class);

    public static final Map<Class<?>, DateTimeFormatter> JAVA_CLASS_TO_FIRESTORE_FORMAT_MAP =
            initJavaClassToFirestoreFormatsMap();

    private static Map<Class<?>, DateTimeFormatter> initJavaClassToFirestoreFormatsMap() {
        final Map<Class<?>, DateTimeFormatter> javaClassToFirestoreFormatMap = new HashMap<>();
        javaClassToFirestoreFormatMap.put(LocalDate.class, DateTimeFormatter.ISO_LOCAL_DATE);
        javaClassToFirestoreFormatMap.put(LocalDateTime.class, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        javaClassToFirestoreFormatMap.put(Instant.class, DateTimeFormatter.ISO_INSTANT);
        return javaClassToFirestoreFormatMap;
    }

    public static List<Field> getNonStaticFields(final Class<?> entityClass) {
        return FieldUtils.getAllFieldsList(entityClass).stream().filter(field -> !isStatic(field))
                .collect(Collectors.toList());
    }

    private static boolean isStatic(final Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    public static void callSetter(final Object obj, final String fieldName, final Object value,
            final Class<?> valueClass) {
        final String setterName = getSetterName(fieldName);
        final Method method;
        try {
            method = obj.getClass().getDeclaredMethod(setterName, valueClass);
            method.invoke(obj, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.warn("Did not set value for field {}", fieldName);
        }
    }

    public static String getSetterName(final String fieldName) {
        return String.format("set%s", CaseUtils.capitalizeWithJavaBeanConvention(fieldName));
    }

}
