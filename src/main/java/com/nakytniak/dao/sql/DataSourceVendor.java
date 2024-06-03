package com.nakytniak.dao.sql;

import com.nakytniak.model.mapping.ConnectionInfo;

public enum DataSourceVendor {
    MYSQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql://%s:%s/%s") {
        @Override
        public String resolveConnectionUrl(final String dbName, final String dbConnection) {
            return dbConnection;
        }
    },
    POSTGRESQL("org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s") {
        @Override
        public String resolveConnectionUrl(final String dbName, final String dbConnection) {
            return dbConnection;
        }
    };

    private final String driverClassName;
    private final String format;

    DataSourceVendor(String driverClassName, String format) {
        this.driverClassName = driverClassName;
        this.format = format;
    }

    public abstract String resolveConnectionUrl(String dbName, String dbConnection);

    public String getDriverClassName() {
        return driverClassName;
    }

    public String generateConnectionUrl(final ConnectionInfo connectionInfo) {
        return String.format(format, connectionInfo.getHost(), connectionInfo.getPort(), connectionInfo.getDatabase());
    }
}
