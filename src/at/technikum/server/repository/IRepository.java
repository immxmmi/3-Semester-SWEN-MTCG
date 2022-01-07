package at.mtcg.utils.server.repository;

import at.mtcg.utils.server.request.Request;
import at.mtcg.utils.server.response.Response;

public interface IRepository {
    Response GET(Request request);

    Response POST(Request request);

    Response PUT(Request request);

    Response INDEX(Request request);

    Response DELETE(Request request);
}
