package com.example.mybank.bean;

public class WebServiceConfiguration {

    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WebServiceConfiguration{" +
                "url='" + url + '\'' +
                '}';
    }
}
