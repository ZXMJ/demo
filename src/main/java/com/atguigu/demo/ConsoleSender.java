package com.atguigu.demo;

public class ConsoleSender implements Sender {

    @Override
    public void send(Event event) {
        System.out.println("[Console] send event: " + event);
    }
}

