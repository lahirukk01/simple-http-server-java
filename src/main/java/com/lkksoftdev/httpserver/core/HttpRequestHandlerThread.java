package com.lkksoftdev.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class HttpRequestHandlerThread extends Thread {
    private final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHandlerThread.class);
    private final Socket socket;

    public HttpRequestHandlerThread(Socket socket) {
        this.socket = socket;
        LOGGER.info("HttpRequestHandlerThread Created");
    }

    private String extractRequestContent(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder requestBuilder = new StringBuilder();
        String line;

        while (br.ready() && (line = br.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\n");
        }
        return requestBuilder.toString();
    }

    @Override
    public void run() {
        LOGGER.info("Handling request...");

        try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {

            String inputContent = extractRequestContent(in);
            LOGGER.info("Received input: {}", inputContent);

            // Construct the HTTP response
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: 13\r\n" + // Example content length
                    "\r\n" +
                    "Hello, World!";

            // Write the response to the OutputStream
            out.write(httpResponse.getBytes());
            out.flush();

            LOGGER.info("Request processing complete");
        } catch (IOException e) {
            LOGGER.error("Error occurred while processing request", e);
        } finally {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                LOGGER.error("Error occurred while closing socket", e);
            }
        }
    }
}
