package org.example;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleHttpsListener {

    public static void main(String[] args) throws Exception {
        // Ustawienia serwera
        int port = 8443; // Wybrany port
        String targetHost = "http://172.104.252.89:8080"; // Host docelowy
        String saveDirectory = "./received_json/"; // Folder na zapis JSON

        // Tworzenie folderu na pliki JSON (jeśli nie istnieje)
        Files.createDirectories(Paths.get(saveDirectory));

        //HTTP configuration
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        // Konfiguracja SSL
//        HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//
//        // Klucz SSL (wymaga klucza w formacie .jks)
//        char[] password = "password".toCharArray();
//        KeyStore ks = KeyStore.getInstance("JKS");
//        ks.load(new FileInputStream("/Users/kolumb/keystore.jks"), password);
//
//        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//        kmf.init(ks, password);
//
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
//        tmf.init(ks);
//
//        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//
//        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
//            public void configure(HttpsParameters params) {
//                try {
//                    SSLContext context = getSSLContext();
//                    SSLEngine engine = context.createSSLEngine();
//                    params.setNeedClientAuth(false);
//                    params.setCipherSuites(engine.getEnabledCipherSuites());
//                    params.setProtocols(engine.getEnabledProtocols());
//                    params.setSSLParameters(context.getDefaultSSLParameters());
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });

        // Ustawienie handlera do obsługi żądań
        server.createContext("/api", new MyHandler(targetHost, saveDirectory));
        server.setExecutor(null); // Domyślny executor
        server.start();

        System.out.println("Server is listening on port: " + port);
    }

    static class MyHandler implements HttpHandler {
        private final String saveDirectory;

        public MyHandler(String targetHost, String saveDirectory) {
            this.saveDirectory = saveDirectory;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Odczyt danych
                InputStream is = exchange.getRequestBody();
                String body = new BufferedReader(new InputStreamReader(is))
                        .lines()
                        .reduce("", (accumulator, actual) -> accumulator + actual);

                // Zapis do pliku JSON
                String fileName = saveDirectory + "request_" + System.currentTimeMillis() + ".json";
                Files.write(Paths.get(fileName), (body + "\n").getBytes(StandardCharsets.UTF_8));


                // Odpowiedź
                String response = "Data received\n";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes(StandardCharsets.UTF_8));
                os.close(); // Zamknięcie strumienia odpowiedzi

                // Zamknięcie połączenia po przetworzeniu danych
                exchange.getRequestBody().close(); // Zamknięcie strumienia wejściowego
                exchange.getResponseBody().close(); // Zamknięcie strumienia wyjściowego
                exchange.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Metoda nieobsługiwana
                exchange.getResponseBody().close();
                exchange.close();
            }
        }
    }
}
