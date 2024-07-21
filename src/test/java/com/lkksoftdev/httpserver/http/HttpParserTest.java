package com.lkksoftdev.httpserver.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {
    private HttpParser httpParser;

    @BeforeAll
    public void beforeAll() {
        httpParser = new HttpParser();
    }

    @Test
    public void testParseHttpGetRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateValidInputStreamGet());
            System.out.println(request);
            assertEquals(HttpMethod.GET, request.getMethod());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseHttpPostRequest() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateValidInputStreamPost());
            System.out.println(request);
            assertEquals(HttpMethod.POST, request.getMethod());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private InputStream generateValidInputStreamGet() {
        String requestStr = """
                GET /?param1=value1&param2=value2 HTTP/1.1\r
                Host: localhost:4500\r
                Connection: keep-alive\r
                Cache-Control: max-age=0\r
                sec-ch-ua: "Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"\r
                sec-ch-ua-mobile: ?0\r
                sec-ch-ua-platform: "macOS"\r
                Upgrade-Insecure-Requests: 1\r
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36\r
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r
                Sec-Fetch-Site: cross-site\r
                Sec-Fetch-Mode: navigate\r
                Sec-Fetch-User: ?1\r
                Sec-Fetch-Dest: document\r
                Accept-Encoding: gzip, deflate, br, zstd\r
                Accept-Language: en-US,en;q=0.9\r
                Cookie: wordpress_test_cookie=WP%20Cookie%20check\r
                \r
                """;

        return new ByteArrayInputStream(requestStr.getBytes(
                StandardCharsets.US_ASCII
        ));
    }

    private InputStream generateValidInputStreamPost() {
        String requestStr = """
                POST /?param1=value1&param2=value2 HTTP/1.1\r
                Host: localhost:4500\r
                Connection: keep-alive\r
                Cache-Control: max-age=0\r
                Content-Length: 20\r
                sec-ch-ua: "Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"\r
                sec-ch-ua-mobile: ?0\r
                sec-ch-ua-platform: "macOS"\r
                Upgrade-Insecure-Requests: 1\r
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36\r
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r
                Sec-Fetch-Site: cross-site\r
                Sec-Fetch-Mode: navigate\r
                Sec-Fetch-User: ?1\r
                Sec-Fetch-Dest: document\r
                Accept-Encoding: gzip, deflate, br, zstd\r
                Accept-Language: en-US,en;q=0.9\r
                Cookie: wordpress_test_cookie=WP%20Cookie%20check\r
                \r
                MyNameIsLahiruKantha
                """;

        return new ByteArrayInputStream(requestStr.getBytes(
                StandardCharsets.US_ASCII
        ));
    }
}