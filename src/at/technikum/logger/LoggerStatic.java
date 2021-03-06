package at.technikum.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class LoggerStatic {

    private static LoggerStatic instance;
    private Logger logData;

    static {
        instance = new LoggerStatic();
    }

    private LoggerStatic(){
        FileHandler history = null;
        try{
            history = new FileHandler("./res/history/history.log",true);
            SimpleFormatter formatter = new SimpleFormatter();
            history.setFormatter(formatter);

        }catch (IOException e){
            e.printStackTrace();
        }
        logData = Logger.getLogger(LoggerStatic.class.getName());
        logData.addHandler(history);
    }

    /** -LOG - MSG  **/
    public void log(String logMessage){
        logData.log(Level.INFO,logMessage);
    }

    public static LoggerStatic getInstance() {
        if(instance == null){
            instance = new LoggerStatic();
        }
        return instance;
    }

}
