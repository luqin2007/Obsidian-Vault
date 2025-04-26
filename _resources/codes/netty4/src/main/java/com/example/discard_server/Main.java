package com.example.discard_server;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        DiscardServer server = new DiscardServer(10001);
        server.run();
    }
}
