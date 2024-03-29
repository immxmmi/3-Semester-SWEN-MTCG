package at.technikum.server;

import at.technikum.control.*;
import at.technikum.server.request.Request;
import at.technikum.server.request.RequestImpl;
import at.technikum.server.request.RequestParser;
import at.technikum.server.response.*;
import at.technikum.server.socket.SocketImpl;
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
    ResponseBuilder responseBuilder;
    @Getter
    Request request;
    @Getter
    SocketImpl clientSocket;
    @Getter
    Map<String, Method> route;

    public ServerHandler(SocketImpl socket) {
        this.responseBuilder = new ResponseBuilderImpl();
        this.clientSocket = (SocketImpl) socket;
        this.request = RequestImpl.builder().build();
        this.response = ResponseImpl.builder().build();
        this.route = new HashMap<>() {
        };
        handler();
    }

    public void setRoutes() throws NoSuchMethodException {
        routePOST();
        routeGET();
        routePUT();
        routeDELETE();
    }

    // ROUTE - PUT
    public void routePOST() {
        try {
            //REGISTER
            this.route.put("^POST /users/?$", PlayerControl.class.getDeclaredMethod("register", RequestImpl.class));
            //LOGIN
            this.route.put("^POST /sessions/?$", PlayerControl.class.getDeclaredMethod("post", RequestImpl.class));
            //PACKAGES
            this.route.put("^POST /packages/?$", PackageControl.class.getDeclaredMethod("post", RequestImpl.class));
            //TRANSAKTION - PACKAGES
            this.route.put("^POST /transactions/packages?$", StoreControl.class.getDeclaredMethod("post", RequestImpl.class));
            // BATTLE - TEST
            this.route.put("^POST /battles?$", BattleControl.class.getDeclaredMethod("post", RequestImpl.class));
            // USER - CREATE TRADING
            this.route.put("^POST /tradings?$", TradeControl.class.getDeclaredMethod("post", RequestImpl.class));
           // USER - TRADE
            this.route.put("^POST /tradings/?$", TradeControl.class.getDeclaredMethod("trade", RequestImpl.class));
            //LOGOUT
            this.route.put("^POST /logout?$", PlayerControl.class.getDeclaredMethod("logout", RequestImpl.class));


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

    public void routeGET() {
        try {
            //CARDS - STACK
            this.route.put("^GET /cards?$", StackControl.class.getDeclaredMethod("get", RequestImpl.class));
            //CARDS - DECK
            this.route.put("^GET /deck?$", DeckControl.class.getDeclaredMethod("get", RequestImpl.class));
            //CARDS - DECK - FORMAT
            this.route.put("^GET /deck-format$", DeckControl.class.getDeclaredMethod("format", RequestImpl.class));
            // USER - PROFIL
            this.route.put("^GET /users/?$", ProfilControl.class.getDeclaredMethod("get", RequestImpl.class));
            // USER - STATUS
            this.route.put("^GET /stats?$", PlayerControl.class.getDeclaredMethod("get", RequestImpl.class));
            // USER - SCORE
            this.route.put("^GET /score?$", PlayerControl.class.getDeclaredMethod("highscore", RequestImpl.class));
            // USER - TRADING LIST
            this.route.put("^GET /tradings?$", TradeControl.class.getDeclaredMethod("get", RequestImpl.class));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void routeDELETE() throws NoSuchMethodException {

        // USER - TRADING - DELETE
        this.route.put("^DELETE /tradings/?$", TradeControl.class.getDeclaredMethod("delete", RequestImpl.class));

    }

    public void routePUT() {
        try {
            //CARDS - DECK - UPDATE
            this.route.put("^PUT /deck?$", DeckControl.class.getDeclaredMethod("put", RequestImpl.class));
            //Profil - UPDATE
            this.route.put("^PUT /users/?$", ProfilControl.class.getDeclaredMethod("put", RequestImpl.class));


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void handler() {
        try {
            // READER
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // READER wird request übergeben
            RequestParser requestParser = new RequestParser();
            request = requestParser.read(reader, request);

            Method m = this.startRoute(request);
            if (m != null) {
                try {
                    this.response = (ResponseImpl) m.invoke(m.getDeclaringClass().getConstructor().newInstance(), request);

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
            ResponseParser responseParser = new ResponseParser();
            responseParser.write(writer,this.response);

        } catch (IOException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Method startRoute(Request request) throws NoSuchMethodException {
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
        if (routeKEY.contains("/deck?format=plain")) {
            if (routeKEY.contains("GET")) {
                routeKEY = "GET /deck-format";
            }
        }
        if (routeKEY.contains("/tradings/")) {
            if (routeKEY.contains("POST")) {
                routeKEY = "POST /tradings/";
            }
            if (routeKEY.contains("DELETE")) {
                routeKEY = "DELETE /tradings/";
            }
        }


        for (Map.Entry<String, Method> entry : this.route.entrySet()) {
            if (Pattern.matches(entry.getKey(), routeKEY)) {
          //      System.out.println("KEY GEFUNDEN");
                return entry.getValue();
            }
        }
        return null;
    }
}