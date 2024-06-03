package com.nakytniak.model.mapping;

import com.nakytniak.dao.sql.DataSourceVendor;

import java.io.Serializable;
import java.util.Map;

public class Mapping implements Serializable {

    private DataSourceVendor sourceVendor;
    private String query;
    private String sourceTable;
    private ConnectionInfo connectionInfo;
    private Map<String, SourceSqlField> tableMappings;

    public Mapping(DataSourceVendor sourceVendor, String query, String sourceTable, ConnectionInfo connectionInfo,
            Map<String, SourceSqlField> tableMappings) {
        this.sourceVendor = sourceVendor;
        this.query = query;
        this.sourceTable = sourceTable;
        this.connectionInfo = connectionInfo;
        this.tableMappings = tableMappings;
    }

    public Mapping() {}

    public DataSourceVendor getSourceVendor() {
        return sourceVendor;
    }

    public void setSourceVendor(DataSourceVendor sourceVendor) {
        this.sourceVendor = sourceVendor;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public Map<String, SourceSqlField> getTableMappings() {
        return tableMappings;
    }

    public void setTableMapping(Map<String, SourceSqlField> tableMappings) {
        this.tableMappings = tableMappings;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "sourceVendor=" + sourceVendor +
                ", query='" + query + '\'' +
                ", sourceTable='" + sourceTable + '\'' +
                ", connectionInfo=" + connectionInfo +
                ", tableMappings=" + tableMappings +
                '}';
    }
}
