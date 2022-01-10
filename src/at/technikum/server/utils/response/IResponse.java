package at.technikum.server.utils.response;

// https://stackoverflow.com/questions/19402482/how-can-i-get-the-full-reason-phrase-or-the-raw-response-with-jax-rs
public interface IResponse {

    void setBody(String body);

    String getContentTyp();


    String getVersion();

    int getStatus();

    String getReasonPhrase();


    String getBody();



}
