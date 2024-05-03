package com.nakytniak.model.mapping;

public enum MySqlDataType {
    INT("INT"),
    BIGINT("BIGINT"),
    SMALLINT("SMALLINT"),
    TINYINT("TINYINT"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    DECIMAL("DECIMAL"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    TIMESTAMP("TIMESTAMP"),
    TIME("TIME"),
    CHAR("CHAR"),
    VARCHAR("VARCHAR"),
    TEXT("TEXT"),
    BINARY("BINARY"),
    VARBINARY("VARBINARY"),
    BLOB("BLOB"),
    ENUM("ENUM"),
    SET("SET");

    private final String typeName;

    MySqlDataType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    // Optional: Custom method to check if a given type name matches any enum constant
    public static MySqlDataType fromTypeName(String typeName) {
        for (MySqlDataType dataType : MySqlDataType.values()) {
            if (dataType.getTypeName().equalsIgnoreCase(typeName)) {
                return dataType;
            }
        }
        throw new IllegalArgumentException("Unknown MySQL data type: " + typeName);
    }
}

