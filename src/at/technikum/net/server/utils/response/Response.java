package at.technikum.net.server.utils.response;

// https://stackoverflow.com/questions/19402482/how-can-i-get-the-full-reason-phrase-or-the-raw-response-with-jax-rs
public interface Response {

    void setBody(String body);

    String getContentTyp();


    String getVersion();

    int getStatus();

    String getReasonPhrase();


    String getBody();



}
