package at.technikum.control.repository;

import at.technikum.net.server.utils.request.RequestImpl;
import at.technikum.net.server.utils.response.ResponseImpl;

public interface Put {

    ResponseImpl put(RequestImpl requestImpl);

}
