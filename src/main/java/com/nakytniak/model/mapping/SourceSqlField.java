package com.nakytniak.model.mapping;

import java.io.Serializable;

public class SourceSqlField implements Serializable {
    private String name;
    private MySqlDataType type;

    public SourceSqlField(String name, MySqlDataType type) {
        this.name = name;
        this.type = type;
    }

    public SourceSqlField() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MySqlDataType getType() {
        return type;
    }

    public void setType(MySqlDataType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SourceSqlField{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
