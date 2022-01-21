package at.technikum.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerLazy {

    private Logger logData;

    private LoggerLazy(){
        FileHandler history = null;
        try{
            history = new FileHandler("history.log",true);
            SimpleFormatter formatter = new SimpleFormatter();
            history.setFormatter(formatter);

        }catch (IOException e){
            e.printStackTrace();
        }
        logData = Logger.getLogger(LoggerStatic.class.getName());
        logData.addHandler(history);
    }

    public void log(String logMessage){
        logData.log(Level.INFO,logMessage);
    }


    public static LoggerLazy getInstance() {
        return LazyHolder.instance;
    }

    private static class LazyHolder {
        private static final LoggerLazy instance = new LoggerLazy();
    }
}
