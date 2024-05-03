package com.nakytniak.dao.sql;

public enum DataSourceVendor {
    MYSQL("com.mysql.cj.jdbc.Driver") {
        @Override
        public String resolveConnectionUrl(final String dbName, final String dbConnection) {
            return dbConnection;
        }
    },
    POSTGRESQL("org.postgresql.Driver") {
        @Override
        public String resolveConnectionUrl(final String dbName, final String dbConnection) {
            return dbConnection;
        }
    };

    private final String driverClassName;

    DataSourceVendor(final String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public abstract String resolveConnectionUrl(String dbName, String dbConnection);

    public String getDriverClassName() {
        return driverClassName;
    }
}
