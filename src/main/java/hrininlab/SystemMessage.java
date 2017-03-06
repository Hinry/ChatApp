package hrininlab;

import hrininlab.Entity.User;

import java.io.LineNumberReader;
import java.io.Serializable;
import java.util.List;

/**
 * Created by mrhri on 06.03.2017.
 */
public class SystemMessage implements Serializable{

    private User user;
    private String message;
    private List<User> userList;

    public SystemMessage(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
