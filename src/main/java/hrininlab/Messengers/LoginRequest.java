package hrininlab.Messengers;

import hrininlab.Entity.User;

import java.io.Serializable;

/**
 * Created by mrhri on 20.03.2017.
 */
public class LoginRequest implements Serializable{

    private User user;
    private String login;
    private String password;
    private String message;

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
