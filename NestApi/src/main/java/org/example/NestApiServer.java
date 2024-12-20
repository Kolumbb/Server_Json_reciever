package org.example;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NestApiServer {

    public static void main(String[] args) throws Exception {
        // Server configuration
        int port = 8443; // Chosen port
        String saveDirectory = "./received_json/"; // Directory for saving JSON files
        //Add logs to the system
        MyLogger myLogger = new MyLogger("NestApiServer.log");

        // Creating a folder for JSON files (if not exist)
        Files.createDirectories(Paths.get(saveDirectory));

        //HTTP configuration
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        // SSL configuration
//        HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//
//      // SSL Key (required key in .jks format)
//        char[] password = "password".toCharArray();
//        KeyStore ks = KeyStore.getInstance("JKS");
        // Configure key store location
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

        // Handler configuration for request handling
        server.createContext("/api", new MyHandler(saveDirectory, myLogger));
        server.setExecutor(null); // Default executor
        server.start();

        myLogger.logInformation(Level.INFO, "Server started");
        myLogger.logInformation(Level.INFO, "Server is listening on port: " + port);

    }

}
