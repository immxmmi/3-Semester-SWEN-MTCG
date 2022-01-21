package at.technikum.control.repository;

import at.technikum.server.request.RequestImpl;
import at.technikum.server.response.ResponseImpl;

public interface Get {

    ResponseImpl get(RequestImpl requestImpl);

}
