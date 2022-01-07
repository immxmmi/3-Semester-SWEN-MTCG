import at.technikum.server.Server;
import at.technikum.utils.logger.LoggerStatic;

public class Main {

    public static void main(String[] args) {
        LoggerStatic loggerStatic = LoggerStatic.getInstance();
        loggerStatic.log("\nStart Game\n");

        Thread service = new Thread((Runnable) new Server(10002, 5, "localhost"));
        service.start();
    }
}