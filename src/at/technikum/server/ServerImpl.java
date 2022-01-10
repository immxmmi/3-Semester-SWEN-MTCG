package at.technikum.server;

import at.technikum.server.utils.socket.Socket;
import at.technikum.utils.tools.TextColor;
import at.technikum.utils.tools.Tools;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

@Builder
@AllArgsConstructor

// http://www.scalingbits.com/java/javakurs2/programmieren2/threads/programmierung
// https://www.javatpoint.com/how-to-create-a-thread-in-java

public class ServerImpl extends Tools implements Server, Runnable {

    //Socket der auf eine Verbindung wartet
    @Getter
    ServerSocket listener;  // Listener
    @Builder.Default
    private int port = 1001; //Port auf dem Sich der Socket andockt
    @Builder.Default
    private String ip = "localhost";  // IP - Adresse an dem sich das Socket andockt
    @Builder.Default
    private int clients = 5; // Anzahl der Clients die sich mit dem Server MAX Verbinden können


    public ServerImpl(int port, int clients, String ip) {
        this.port = port;
        this.clients = clients;
        this.ip = ip;
    }


    @Override
    // Listener
    public void listen() {

        try {
            // IP --> Eingebunden
            InetAddress bindAddr = InetAddress.getByName(ip);
            // Listener initialisiert
            this.listener = new ServerSocket(port, clients, bindAddr);
            // Server wartet auf eine Verbindung
            System.out.println(TextColor.ANSI_GREEN + "\nServer -> Listen on IP + PORT " + this.listener.getLocalSocketAddress() + "..." + TextColor.ANSI_RESET);

            // Solange die Verbindung nicht abgebrochen wird können sich Clients verbinden
            while (true) {
                // Socket für den Client wird erstellt
                Socket clientSocket = new Socket(this.listener.accept());
                // Benachrichtigung, welcher Client sich mit dem Server verbunden hat
                System.out.println(TextColor.ANSI_BLUE + "\nNEW CLIENT CONNECTION - IP + PORT " + this.listener.getLocalSocketAddress() + "..." + TextColor.ANSI_RESET);
                // ClientSocket wird dem Handler übergeben --> Threading wird auch gleich erstellt
                Thread currentClient = new Thread(() -> new ServerHandler(clientSocket));
                // Client wird gestartet
                currentClient.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    // Close
    public void closeServer() {
        System.out.println(TextColor.ANSI_RED + "\nServer -> Closing on PORT " + listener.getLocalPort() + "..." + TextColor.ANSI_RESET);
        try {
            // Listener wird geschlossen
            listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    // Threading --> run
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeServer));
        listen();
    }

}
