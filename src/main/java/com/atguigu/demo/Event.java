package com.atguigu.demo;

import java.util.Map;

public class Event {

    String name;
    long timestamp;
    Map<String, String> properties;

    public Event(String name, Map<String, String> properties) {
        this.name = name;
        this.properties = properties;
        this.timestamp = System.currentTimeMillis();
    }

    // demo 用，简单拼一下
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"event\":\"").append(name).append("\",");
        sb.append("\"time\":").append(timestamp).append(",");
        sb.append("\"props\":").append(properties);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toJson();
    }
}
