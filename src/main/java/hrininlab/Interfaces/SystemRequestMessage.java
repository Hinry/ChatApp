package hrininlab.Interfaces;

import hrininlab.Entity.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mrhri on 17.03.2017.
 */
public class SystemRequestMessage implements Serializable {

    private User user;
    private User user2;
    private String message;
    private List<User> userlist;

    public SystemRequestMessage() {
    }

    public SystemRequestMessage(User user, User user2, String message, List<User> userlist) {
        this.user = user;
        this.user2 = user2;
        this.message = message;
        this.userlist = userlist;
    }

    public User getUser() {
        return user;
    }

    public User getUser2() {
        return user2;
    }

    public String getMessage() {
        return message;
    }

    public List<User> getUserlist() {
        return userlist;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserlist(List<User> userlist) {
        this.userlist = userlist;
    }
}
