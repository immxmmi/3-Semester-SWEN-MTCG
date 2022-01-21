package at.technikum.net.server.utils.request;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {

    public RequestParser() {
    }

    public Request read(BufferedReader reader, Request request) {

        try {

            StringBuilder currentBuffer = new StringBuilder();
            int data;
            int counter = 0; // zählt die Zeilen die es gibt
            boolean contentReached = false;

            // Kopiert den ganzen URL in einem BUFFER
            while ((data = reader.read()) != -1) {
                currentBuffer.append((char) data);
                if (!reader.ready()) break;
            }

            //currentBuffer in einem String speichern
            //Jede Zeile Spliten
            String[] lines = currentBuffer.toString().split("\n");
            int bodyIndex = lines.length - 1;
            String[] Head = lines[0].toString().split(" ");
            request.setMethod(Head[0].toString().replace("\r", ""));
            request.setPath(Head[1].toString().replace("\r", ""));
            request.setVersion(Head[2].toString().replace("\r", ""));
            String[] Host = lines[1].toString().split(":");
            request.setHost(Host[1].toString().replace("\r", "").replace(" ", ""));


            int authIndex = -1;

            for (int i = 0; i < bodyIndex; i++) {
                if (lines[i].contains("Authorization: Basic")) {
                    authIndex = i;
                    break;
                }
                ;
            }

            if (authIndex != -1) {
                String[] Auth = lines[authIndex].toString().split(": Basic");
                request.setAuth(Auth[1].toString().replace("\r", "").replace(" ",""));
            } else {
                request.setAuth(null);
            }


            if (!lines[bodyIndex].matches("/r")) {
                request.setBody(lines[bodyIndex].toString().replace("\r", ""));
            } else {
                request.setBody("");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return request;
    }
}
