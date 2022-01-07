package at.technikum.server;

import at.technikum.server.request.Request;
import at.technikum.server.response.Response;
import at.technikum.server.socket.Socket;
import at.technikum.utils.battle.service.BattleServlet;
import at.technikum.utils.packages.service.PackageServlet;
import at.technikum.utils.player.service.PlayerServlet;
import at.technikum.utils.store.service.StoreServlet;
import lombok.Getter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// Diese Klasse verarbeitet den request Part und schickt eine Response

public class ServerHandler {

    @Getter
    Response response;
    @Getter
    Request request;
    @Getter
    Socket clientSocket;
    @Getter
    Map<String, Method> route;

    public ServerHandler(Socket socket) {
        this.clientSocket = (Socket) socket;
        this.request = new Request();
        this.response = new Response();
        this.route = new HashMap<>() {
        };
        handler();
    }

    public void setRoutes() {
        routePOST();
        routeGET();
        routePUT();
        routeDELETE();
    }

    // ROUTE - PUT
    public void routePOST() {
        try {
            //REGISTER
            this.route.put("^POST /users/?$", PlayerServlet.class.getDeclaredMethod("POST", Request.class));
            //LOGIN
            this.route.put("^POST /sessions/?$", PlayerServlet.class.getDeclaredMethod("LOGIN", Request.class));
            //PACKAGES
            this.route.put("^POST /packages/?$", PackageServlet.class.getDeclaredMethod("POST", Request.class));
            //TRANSAKTION - PACKAGES
            this.route.put("^POST /transactions/packages?$", StoreServlet.class.getDeclaredMethod("POST", Request.class));
            // BATTLE - TEST
            this.route.put("^POST /battles?$", BattleServlet.class.getDeclaredMethod("POST", Request.class));

            //this.route.put("^POST //tradings?$", StoreServlet.class.getDeclaredMethod("POST", Request.class));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

    public void routeGET() {
        try {
            //CARDS - STACK
            this.route.put("^GET /cards?$", PlayerServlet.class.getDeclaredMethod("STACK", Request.class));
            //CARDS - DECK
            this.route.put("^GET /deck?$", PlayerServlet.class.getDeclaredMethod("DECK", Request.class));
            // USER - DATA
            this.route.put("^GET /users/?$", PlayerServlet.class.getDeclaredMethod("GET", Request.class));
            // USER - STATUS
            this.route.put("^GET /stats?$", PlayerServlet.class.getDeclaredMethod("STATUS", Request.class));
            // USER - SCORE
            this.route.put("^GET /score?$", PlayerServlet.class.getDeclaredMethod("SCORE", Request.class));
            // this.route.put("^GET /tradings?$", PlayerServlet.class.getDeclaredMethod("DECK", Request.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void routeDELETE() {
    }

    public void routePUT() {
        try {
            //CARDS - DECK
            this.route.put("^PUT /deck?$", PlayerServlet.class.getDeclaredMethod("SETDECK", Request.class));
            //USER - DATA
            this.route.put("^PUT /users/?$", PlayerServlet.class.getDeclaredMethod("PUT", Request.class));


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void handler() {
        try {
            // READER
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // READER wird request übergeben
            request.read(reader);

            Method m = this.startRoute(request);
            if (m != null) {
                try {
                    this.response = (Response) m.invoke(m.getDeclaringClass().getConstructor().newInstance(), request);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            } else {
                this.response = this.response.statusBAD();
            }


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.response.write(writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Method startRoute(Request request) {
        System.out.println("# START - ROUTING");
        if (request.getMethod() == null) {
            return null;
        }
        if (request.getPath() == null) {
            return null;
        }

        //SET ROUTE
        setRoutes();

        // SEARCH KEY
        String routeKEY = request.getMethod() + " " + request.getPath();
        //System.out.println("KEY - ROUTE: " + routeKEY);
        // VERGLEICHT SEARCH KEY MIT ROUTING KEY
        if (routeKEY.contains("/users")) {
            if (routeKEY.contains("GET")) {
                routeKEY = "GET /users/";
            }
            if (routeKEY.contains("PUT")) {
                routeKEY = "PUT /users/";
            }
        }


        for (Map.Entry<String, Method> entry : this.route.entrySet()) {
            if (Pattern.matches(entry.getKey(), routeKEY)) {
                System.out.println("KEY GEFUNDEN");
                return entry.getValue();
            }
        }
        return null;
    }
}

