package org.example;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;

class MyHandler implements HttpHandler {
    private final String saveDirectory;
    private MyLogger myLogger;

    public MyHandler(String saveDirectory, MyLogger myLogger) {
        this.saveDirectory = saveDirectory;
        this.myLogger = myLogger;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {

            String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
            myLogger.logInformation(Level.INFO, "Connection initiated with IP: " + ip);

            // Reading data

            InputStream is = exchange.getRequestBody();
            String body = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);


            // Save JSON to file
            String fileName = saveDirectory + "request_" + System.currentTimeMillis() + ".json";
            Files.writeString(Paths.get(fileName), body + "\n");
            myLogger.logInformation(Level.INFO, "Data saved in " + fileName);

            // Answer from server
            String response = "Data received\n";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            myLogger.logInformation(Level.INFO, "Response from client: " + exchange.getResponseCode());

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            myLogger.logInformation(Level.INFO, "Connection is closed");

            // Closing connection after receiving data
            close(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Metoda nieobsługiwana
            myLogger.logInformation(Level.WARNING, "Something feels odd: " + exchange.getResponseCode());

            close(exchange);
        }
    }
    static void close(HttpExchange exchange) throws IOException {
        exchange.getRequestBody().close();
        exchange.getResponseBody().close();
        exchange.close();
    }

}


