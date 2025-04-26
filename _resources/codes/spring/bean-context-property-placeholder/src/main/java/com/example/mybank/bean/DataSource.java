package com.example.mybank.bean;

public class DataSource {

    private String url;
    private String username;
    private String password;
    private String driverClass;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                " \n     url='" + url + '\'' +
                ",\n    username='" + username + '\'' +
                ",\n    password='" + password + '\'' +
                ",\n    driverClass='" + driverClass + '\'' +
                "\n}";
    }
}
