package com.nakytniak.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SourceTableInfo implements Serializable {

    @Serial private static final long serialVersionUID = 2601288582331180847L;

    private Map<String, String> javaFieldToSourceField;
    private Set<String> javaFields;
    private String tableName;
    private String idFieldName;

    public SourceTableInfo(final Map<String, String> javaFieldToSourceField, final String tableName,
            final String idFieldName) {
        this.javaFieldToSourceField = javaFieldToSourceField;
        this.tableName = tableName;
        this.idFieldName = idFieldName;
    }

    public SourceTableInfo(final Set<String> javaFields, final String tableName) {
        this.javaFields = javaFields;
        this.tableName = tableName;
    }

    public Map<String, String> getJavaFieldToSourceField() {
        return javaFieldToSourceField;
    }

    public void setJavaFieldToSourceField(final Map<String, String> javaFieldToSourceField) {
        this.javaFieldToSourceField = javaFieldToSourceField;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(final String idFieldName) {
        this.idFieldName = idFieldName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SourceTableInfo that = (SourceTableInfo) o;
        return Objects.equals(javaFieldToSourceField, that.javaFieldToSourceField)
                && Objects.equals(tableName, that.tableName) && Objects.equals(idFieldName, that.idFieldName)
                && Objects.equals(javaFields, that.javaFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(javaFieldToSourceField, tableName, idFieldName, javaFields);
    }
}