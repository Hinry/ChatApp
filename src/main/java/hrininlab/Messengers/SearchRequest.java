package hrininlab.Messengers;

import hrininlab.Entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mrhri on 21.03.2017.
 */
public class SearchRequest implements Serializable{


    private String login;
    private String name;
    private String lastName;
    private User user;
    private List<User> userlist;

    public SearchRequest(String login, String name, String lastName) {
        this.login = login;
        this.name = name;
        this.lastName = lastName;
    }

    public SearchRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<User> userlist) {
        this.userlist = userlist;
    }
}
