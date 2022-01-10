package at.technikum.server.utils.response;

import at.technikum.utils.tools.Tools;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
public class Response extends Tools implements IResponse{
    private static final String delimiter = "\n";
    private static final String end = "\r\n";

    @Getter
    @Builder.Default
    String version = "HTTP/1.1";
    @Getter
    @Builder.Default
    String server = "localhost";
    @Getter
    @Builder.Default
    String contentTyp = "application/json; charset=utf-8";
    @Getter
    @Builder.Default
    String content = "<!DOCTYPE html><html><body><h1>Hello, World!</h1></body></html>";
    @Getter
    @Builder.Default
    int contentLength = 0;

    @Getter
    @Builder.Default
    int status = 200;
    @Getter
    @Builder.Default
    String reasonPhrase = "OK";

    @Getter
    @Setter
    @Builder.Default
    String body = "";

}
