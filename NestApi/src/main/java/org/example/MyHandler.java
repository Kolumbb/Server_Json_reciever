package org.example;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

class MyHandler implements HttpHandler {
    private final String saveDirectory;

    public MyHandler(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            // Reading data
            InputStream is = exchange.getRequestBody();
            String body = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);

            // Save JSON to file
            String fileName = saveDirectory + "request_" + System.currentTimeMillis() + ".json";
            Files.writeString(Paths.get(fileName), body + "\n");

            // Answer from server
            String response = "Data received\n";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            // Closing connection after receiving data
            close(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Metoda nieobsługiwana
            close(exchange);
        }
    }
    static void close(HttpExchange exchange) throws IOException {
        exchange.getRequestBody().close();
        exchange.getResponseBody().close();
        exchange.close();
    }
}

