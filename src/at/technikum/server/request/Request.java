package at.technikum.server.request;

import lombok.Builder;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

// https://www.ionos.at/digitalguide/hosting/hosting-technik/http-request-erklaert/
// - TEST HEADER

// curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Basic kienboec-mtcgToken"
public class Request implements IRequest {

    @Getter
    @Builder.Default
    String host = "localhost";
    @Getter
    String version;
    @Getter
    String method;
    @Getter
    String path;
    @Getter
    String body;
    @Getter
    String auth;
    @Getter
    HashMap<String, String> params;
    @Getter
    HashMap<String, String> headers;


    public Request() {
        this.params = new HashMap<>();
        this.headers = new HashMap<>();
    }


    @Override
    public void read(BufferedReader reader) {

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
            System.out.println(currentBuffer);
            //Jede Zeile Spliten
            String[] lines = currentBuffer.toString().split("\n");
            int bodyIndex = lines.length - 1;
            String[] Head = lines[0].toString().split(" ");
            this.method = Head[0].toString().replace("\r", "");
            ;
            this.path = Head[1].toString().replace("\r", "");
            ;
            this.version = Head[2].toString().replace("\r", "");
            ;
            String[] Host = lines[1].toString().split(":");
            this.host = Host[1].toString().replace("\r", "").replace(" ", "");
            ;

            int authIndex = -1;

            for (int i = 0; i < bodyIndex; i++) {
                if (lines[i].contains("Authorization")) {
                    authIndex = i;
                    break;
                }
                ;
            }

            if (authIndex != -1) {
                String[] Auth = lines[authIndex].toString().split(": ");
                this.auth = Auth[1].toString().replace("\r", "");
            } else {
                this.auth = null;
            }


            if (!lines[bodyIndex].matches("/r")) {
                this.body = lines[bodyIndex].toString().replace("\r", "");
            } else {
                this.body = "";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}