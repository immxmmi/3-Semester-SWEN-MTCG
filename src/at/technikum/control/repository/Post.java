package at.technikum.control.repository;

import at.technikum.server.request.RequestImpl;
import at.technikum.server.response.ResponseImpl;

public interface Post {

    ResponseImpl post(RequestImpl requestImpl);
}
