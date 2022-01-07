import at.technikum.server.Server;

public class Main {

    public static void main(String[] args) {
        Thread service = new Thread((Runnable) new Server(10003, 5, "localhost"));
        service.start();
    }
}
