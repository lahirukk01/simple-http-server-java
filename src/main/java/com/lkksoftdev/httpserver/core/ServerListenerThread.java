package com.lkksoftdev.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerListenerThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    private final ServerSocket serverSocket;
    private final int requestLimit;
    private final int port;
    private final ExecutorService threadPool;

    public ServerListenerThread(int port, int requestLimit) throws IOException {
        this.port = port;
        this.requestLimit = requestLimit;
        this.serverSocket = new ServerSocket(port);
        this.threadPool = Executors.newFixedThreadPool(requestLimit);

        LOGGER.info("ServerListenerThread constructed for port {}", port);
    }

    @Override
    public void run() {
        try {
            LOGGER.info("ServerListenerThread running on port {}", port);

            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                LOGGER.info("Connection accepted: {}", socket.getInetAddress());
                threadPool.execute(new HttpRequestHandlerThread(socket));
//                (new HttpRequestHandlerThread(socket)).start();
            }
        } catch (IOException e) {
            LOGGER.error("Error occurred while running server listener thread", e);
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                LOGGER.info("Closing server socket...");
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    LOGGER.error("Error occurred while closing server socket", e);
                }
            }
        }
    }
}
