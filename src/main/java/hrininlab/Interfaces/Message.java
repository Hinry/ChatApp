package hrininlab.Interfaces;


import hrininlab.Entity.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mrhri on 03.03.2017.
 */
public class Message implements Serializable {

    private User user;
    private User userprivate;
    private String message;
    private List<User> usersOnlineList;


    public Message(User user, String message) {
        this.user = user;
        this.message = message;
    }
    public Message(User user, String message, List<User> list){
        this.user = user;
        this.message = message;
        this.usersOnlineList = list;
    }

    public Message() {
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

    public List<User> getUsersOnlineList() {
        return usersOnlineList;
    }

    public void setUsersOnlineList(List<User> usersOnlineList) {
        this.usersOnlineList = usersOnlineList;
    }

    public User getUserprivate() {
        return userprivate;
    }

    public void setUserprivate(User userprivate) {
        this.userprivate = userprivate;
    }

}
