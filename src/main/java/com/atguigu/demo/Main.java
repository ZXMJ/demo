package com.atguigu.demo;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        EventReporter reporter = new EventReporter();
        reporter.addSender(new ConsoleSender());
        reporter.addSender(new NetworkSender());

        System.out.println("start reporting events");

        for (int i = 0; i < 5; i++) {
            Map<String, String> props = new HashMap<>();
            props.put("index", String.valueOf(i));
            Event e = new Event("test_event_" + i, props);
            reporter.report(e);
            Thread.sleep(200);
        }

        System.out.println("events sent, press enter to exit");
        System.in.read();
    }
}

