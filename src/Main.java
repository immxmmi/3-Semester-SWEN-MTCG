import at.technikum.server.ServerImpl;
import at.technikum.logger.LoggerStatic;

public class Main {

    public static void main(String[] args) {
        LoggerStatic loggerStatic = LoggerStatic.getInstance();
        loggerStatic.log("\nStart SERVER\n");

        Thread service = new Thread((Runnable) new ServerImpl(10001, 5, "localhost"));
        service.start();
    }
}