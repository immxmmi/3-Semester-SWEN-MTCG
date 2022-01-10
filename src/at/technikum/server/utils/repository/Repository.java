package at.technikum.server.utils.repository;

import at.technikum.server.utils.request.Request;
import at.technikum.server.utils.response.Response;
import at.technikum.server.utils.response.ResponseBuilder;
import at.technikum.utils.tools.Tools;

public abstract class Repository extends Tools implements IRepository { // TODO: 07.01.2022 Repository kürzen und IResponse
    @Override
    public Response GET(Request request) {
        return new ResponseBuilder().statusNotFound("");
    }

    @Override
    public Response POST(Request request) {
        return new ResponseBuilder().statusNotFound("");
    }

    @Override
    public Response PUT(Request request) {
        return new ResponseBuilder().statusNotFound("");
    }

    @Override
    public Response INDEX(Request request) {
        return new ResponseBuilder().statusNotFound("");
    }

    @Override
    public Response DELETE(Request request) {
        return new ResponseBuilder().statusNotFound("");
    }


}
