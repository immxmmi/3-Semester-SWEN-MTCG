package at.technikum.server.response;

import java.io.BufferedWriter;
import java.io.IOException;

public class ResponseParser {
    public ResponseParser() {
    }
    public void write(BufferedWriter writer, Response response) {

        try {
            if(response.getBody() == null){
                response.setBody("");
            }
            StringBuilder currentBuffer = new StringBuilder();
            currentBuffer.append(response.getVersion() +" "+ response.getStatus()+" "+ response.getReasonPhrase() + "\r\n");   // Status
            currentBuffer.append("Content-Type: "+response.getContentTyp()+"\r\n");
            currentBuffer.append("Content-Length: "+response.getBody().length()+"\r\n");
            currentBuffer.append("\r\n"+response.getBody()+"\r\n");

            writer.write(currentBuffer.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
