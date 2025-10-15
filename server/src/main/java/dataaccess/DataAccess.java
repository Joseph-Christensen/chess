package dataaccess;

import model.*;

public interface DataAccess {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
    void createAuth(AuthData user);
    AuthData getAuth(String username);
    String getPassword(String username);
}
