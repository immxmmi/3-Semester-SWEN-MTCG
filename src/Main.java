import at.technikum.net.server.ServerImpl;
import at.technikum.utils.logger.LoggerStatic;

public class Main {

    public static void main(String[] args) {
        LoggerStatic loggerStatic = LoggerStatic.getInstance();
        loggerStatic.log("\nStart Game\n");

        Thread service = new Thread((Runnable) new ServerImpl(10001, 5, "localhost"));
        service.start();
    }
}