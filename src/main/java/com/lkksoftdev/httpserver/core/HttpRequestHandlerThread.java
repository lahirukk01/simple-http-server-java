package com.lkksoftdev.httpserver.core;

import com.lkksoftdev.httpserver.http.HttpParser;
import com.lkksoftdev.httpserver.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class HttpRequestHandlerThread extends Thread {
    private final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHandlerThread.class);
    private final Socket socket;

    private static final String EMPTY_RESPONSE = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/plain\r\n" +
            "Content-Length: 0\r\n" + // Example content length
            "\r\n";

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
            HttpParser parser = new HttpParser();
            HttpRequest request = parser.parseHttpRequest(in);

            String httpResponse = EMPTY_RESPONSE;

            if (request != null) {
                LOGGER.info("Request: {}", request);
                httpResponse = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: 13\r\n" + // Example content length
                        "\r\n" + "Hello, World!";
            } else {
                LOGGER.info("Request is null");
            }

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
