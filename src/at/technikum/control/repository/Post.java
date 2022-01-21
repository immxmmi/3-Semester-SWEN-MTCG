package at.technikum.control.repository;

import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseImpl;

public interface Post {

    ResponseImpl post(RequestImpl requestImpl);
}
