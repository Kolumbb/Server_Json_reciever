package org.example;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
    public Logger logger;
    FileHandler fileHandler;

    public MyLogger(String fileName) throws IOException {
        File file = new File(fileName);
        if(! file.exists()) file.createNewFile();

        fileHandler = new FileHandler(fileName, true);
        logger = Logger.getLogger("NestApiLogger");
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);
        SimpleFormatter formatter= new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }

    public void logInformation(Level level, String msg){
        logger.setLevel(level);
        if (level == Level.INFO) logger.info(msg);
        if (level == Level.WARNING) logger.warning(msg);
    }
}
