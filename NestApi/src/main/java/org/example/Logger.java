package org.example;

import java.util.logging.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;

public class Logger {
    public java.util.logging.Logger logger;
    public Logger(String fileName) throws SecurityException, IOException{
        File file = new File(fileName);
        if(!file.exists()) file.createNewFile();

        FileHandler fh = new FileHandler(fileName, true);
        logger = java.util.logging.Logger.getLogger("Test");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }
}
