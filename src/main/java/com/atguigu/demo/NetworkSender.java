package com.atguigu.demo;

import java.util.Random;

public class NetworkSender implements Sender {

    private Random random = new Random();

    @Override
    public void send(Event event) throws Exception {
        // 模拟网络不稳定
        if (random.nextInt(100) < 30) {
            throw new Exception("network error");
        }
        System.out.println("[Network] send event: " + event);
    }
}

