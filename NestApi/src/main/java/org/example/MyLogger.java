package org.example;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class MyLogger {
    public Logger logger;
    FileHandler fileHandler;

    public MyLogger(String fileName) throws IOException {
        logger = Logger.getLogger("NestApiLogger");
        File file = new File(fileName);
        if(! file.exists()) file.createNewFile();

        fileHandler = new FileHandler(fileName, true);
        fileHandler.setFormatter(
                new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        return String.format("%1$tF %1$tT [%2$s] %3$s: %4$s",
                                record.getMillis(), // Timestamp in YYYY-MM-DD HH:MM:SS format
                                record.getLevel(),   // Log level (INFO, WARNING, etc.)
                                record.getLoggerName(),  // Logger name
                                record.getMessage());  // Log message
                    }
                }
        );
        logger.addHandler(fileHandler);
    }

    public void logInformation(Level level, String msg){
        logger.setLevel(level);
        if (level == Level.INFO) logger.info(msg + System.lineSeparator());
        if (level == Level.WARNING) logger.warning(msg + System.lineSeparator());
    }
}
