package at.technikum.server;

import at.technikum.control.*;
import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.IResponse;
import at.technikum.server.utils.response.IResponseBuilder;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.server.utils.socket.Socket;
import at.technikum.utils.battle.service.BattleServlet;
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
    IResponse response;
    @Getter
    IResponseBuilder responseBuilder;
    @Getter
    Request request;
    @Getter
    Socket clientSocket;
    @Getter
    Map<String, Method> route;

    public ServerHandler(Socket socket) {
        this.responseBuilder = new ResponseBuilder();
        this.clientSocket = (Socket) socket;
        this.request = new Request();
        this.response = Response.builder().build();
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
            this.route.put("^POST /users/?$", PlayerControl.class.getDeclaredMethod("register", Request.class));
            //LOGIN
            this.route.put("^POST /sessions/?$", PlayerControl.class.getDeclaredMethod("login", Request.class));
            //PACKAGES
            this.route.put("^POST /packages/?$", PackageControl.class.getDeclaredMethod("post", Request.class));
            //TRANSAKTION - PACKAGES
            this.route.put("^POST /transactions/packages?$", StoreControl.class.getDeclaredMethod("post", Request.class));
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
            this.route.put("^GET /cards?$", StackControl.class.getDeclaredMethod("get", Request.class));
            //CARDS - DECK
            this.route.put("^GET /deck?$", DeckControl.class.getDeclaredMethod("get", Request.class));
            // USER - PROFIL
            this.route.put("^GET /users/?$", ProfilControl.class.getDeclaredMethod("get", Request.class));
            // USER - STATUS
            this.route.put("^GET /stats?$", PlayerControl.class.getDeclaredMethod("status", Request.class));
            // USER - SCORE
            this.route.put("^GET /score?$", PlayerControl.class.getDeclaredMethod("highscore", Request.class));



            // this.route.put("^GET /tradings?$", PlayerServlet.class.getDeclaredMethod("DECK", Request.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void routeDELETE() {}

    public void routePUT() {
        try {
            //CARDS - DECK - UPDATE
            this.route.put("^PUT /deck?$", DeckControl.class.getDeclaredMethod("put", Request.class));
            //Profil - UPDATE
            this.route.put("^PUT /users/?$", ProfilControl.class.getDeclaredMethod("put", Request.class));


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
                this.response = this.responseBuilder.statusBAD("BAD REQUEST");
            }


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.responseBuilder.write(writer,this.response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Method startRoute(Request request) {
       // System.out.println("# START - ROUTING");
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
            //    System.out.println("KEY GEFUNDEN");
                return entry.getValue();
            }
        }
        return null;
    }
}