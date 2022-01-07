package at.mtcg.utils.server.repository;

import at.mtcg.utils.server.request.Request;
import at.mtcg.utils.server.response.Response;
import at.mtcg.utils.tools.Tools;

public abstract class Repository extends Tools implements IRepository {
    @Override
    public Response GET(Request request) {
        return new Response().statusNotFound();
    }

    @Override
    public Response POST(Request request) {
        return new Response().statusNotFound();
    }

    @Override
    public Response PUT(Request request) {
        return new Response().statusNotFound();
    }

    @Override
    public Response INDEX(Request request) {
        return new Response().statusNotFound();
    }

    @Override
    public Response DELETE(Request request) {
        return new Response().statusNotFound();
    }


}
