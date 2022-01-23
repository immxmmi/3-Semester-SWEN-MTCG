import at.technikum.logger.LoggerStatic;
import at.technikum.server.ServerImpl;

public class Main {

    public static void main(String[] args) {
        LoggerStatic loggerStatic = LoggerStatic.getInstance();
        loggerStatic.log("\nStart SERVER\n");

        Thread service = new Thread((Runnable) new ServerImpl(10001, 5, "localhost"));
        service.start();
    }
}