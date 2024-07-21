package com.lkksoftdev.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListener.class);
    private final ServerSocket serverSocket;
    private final int requestLimit;
    private final int port;

    public ServerListener(int port, int requestLimit) throws IOException {
        this.port = port;
        this.requestLimit = requestLimit;
        this.serverSocket = new ServerSocket(port);

        LOGGER.info("ServerListenerThread constructed for port {}", port);
    }

    public void listen() {
        try {
            LOGGER.info("ServerListenerThread running on port {}", port);

            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                LOGGER.info("Connection accepted: {}", socket.getInetAddress());
                (new HttpRequestHandler(socket)).start();
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
