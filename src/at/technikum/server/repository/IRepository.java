package at.technikum.server.repository;

import at.technikum.server.request.Request;
import at.technikum.server.response.Response;

public interface IRepository { // TODO: 07.01.2022 Repository kürzen
    Response GET(Request request);

    Response POST(Request request);

    Response PUT(Request request);

    Response INDEX(Request request);

    Response DELETE(Request request);
}
