package com.nakytniak.model.mapping;

import java.io.Serializable;

public class ConnectionInfo implements Serializable {
    private String username;
    private String password;
    private String host;
    private Integer port;
    private String database;

    public ConnectionInfo(String username, String password, String host, Integer port, String database) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.database = database;
    }

    public ConnectionInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return "ConnectionInfo{}";
    }
}
