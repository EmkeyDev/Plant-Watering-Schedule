package manager;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class AuthManager {
    private List<User> users = new ArrayList<>();
    private User currentUser = null;

    public AuthManager() {
        users.add(new User("Emir", "emir123", "ADMIN"));
        users.add(new User("user","user123","USER"));
    }

    public User login(String username,String password){
        for (User user:users){
            if (user.getUsername().equals(username)&& user.getPassword().equals(password)){
                currentUser = user;
                return user;
            }
        }
        return null;
    }

    public User getCurrentUser(){
        return currentUser;
    }
}
