package com.example.echo_server;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        EchoServer server = new EchoServer(10001);
        server.run();
    }
}
