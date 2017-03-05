package hrininlab;

import com.sun.xml.internal.ws.developer.Serialization;
import hrininlab.Entity.User;
import javafx.collections.ObservableList;

import javax.jws.soap.SOAPBinding;
import java.io.Serializable;

/**
 * Created by mrhri on 03.03.2017.
 */
public class Message implements Serializable {

    private User user;
    private String message;
    private ObservableList<User> usersOnlineList;

    public Message(User user, String message) {
        this.user = user;
        this.message = message;
    }
    public Message(User user, String message, ObservableList<User> list){
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

    public ObservableList<User> getUsersOnlineList() {
        return usersOnlineList;
    }

    public void setUsersOnlineList(ObservableList<User> usersOnlineList) {
        this.usersOnlineList = usersOnlineList;
    }
}
