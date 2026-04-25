package com.atguigu.demo;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventReporter {

    private BlockingQueue<Event> queue = new LinkedBlockingQueue<>();
    private List<Sender> senders = new ArrayList<>();
    private File file = new File("events.log");

    public EventReporter() {
        loadFromFile();
        startWorker();
    }

    public void addSender(Sender sender) {
        senders.add(sender);
    }

    public void report(Event event) {
        try {
            queue.put(event);
            saveToFile(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 后台线程
    private void startWorker() {
        Thread t = new Thread(() -> {
            System.out.println("worker thread started");
            while (true) {
                try {
                    Event event = queue.take();
                    handleEvent(event);
                    removeFirstLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void handleEvent(Event event) {
        for (Sender sender : senders) {
            int retry = 0;
            while (retry < 3) {
                try {
                    sender.send(event);
                    break;
                } catch (Exception e) {
                    retry++;
                    System.out.println("send failed, retry " + retry);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }

    // ====== 持久化相关 ======

    private synchronized void saveToFile(Event event) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(event.toJson());
            fw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void loadFromFile() {
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // demo 简单处理，直接跳过解析失败的情况
                Event e = new Event("recovered_event", new HashMap<>());
                queue.offer(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void removeFirstLine() {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }
            if (lines.isEmpty()) return;

            try (FileWriter fw = new FileWriter(file)) {
                for (int i = 1; i < lines.size(); i++) {
                    fw.write(lines.get(i));
                    fw.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

