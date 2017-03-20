package hrininlab.Entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mrhri on 29.01.2017.
 */
@Entity
@Table(name = "app_user")
public class User implements Serializable{

    @NotEmpty
    @Column(name = "login", unique = true)
    private String login;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @Column(name = "online")
    private boolean online;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @NotEmpty
    @Column(name = "first_name")
    private String first_name;

    @NotEmpty
    @Column(name = "last_name")
    private String last_name;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @OneToMany(cascade={CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @IndexColumn(name="user_id")
    private Set<ContactList> contactList = new HashSet<ContactList>();

    @OneToMany(cascade={CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @IndexColumn(name="which_user_id")
    private Set<ChatHistory> chatHistories = new HashSet<ChatHistory>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_profiles",
            joinColumns = { @JoinColumn(name = "USER_ID") },
            inverseJoinColumns = { @JoinColumn(name = "USER_ROLE_ID") })
    private Set<UserRole> userRoles = new HashSet<UserRole>();


    @Column(name = "role")
    private String rol;

    public String getRol() {
        for(UserRole role : this.userRoles){
            rol = role.getType();
        }
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public User(String login, String first_name, String last_name, String password) {
        this.login = login;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
    }

    public User(String login) {
        this.login = login;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public String getLogin(){
        return login;
    }

    public StringProperty getPropertyLogin(){
        return new SimpleStringProperty(this.login);
    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ContactList> getContactList() {
        return contactList;
    }

    public void setContactList(Set<ContactList> contactList) {
        this.contactList = contactList;
    }

    public Set<ChatHistory> getChatHistories() {
        return chatHistories;
    }

    public void setChatHistories(Set<ChatHistory> chatHistories) {
        this.chatHistories = chatHistories;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getId() != user.getId()) return false;
        if (getLogin() != null ? !getLogin().equals(user.getLogin()) : user.getLogin() != null) return false;
        if (getFirst_name() != null ? !getFirst_name().equals(user.getFirst_name()) : user.getFirst_name() != null)
            return false;
        if (getLast_name() != null ? !getLast_name().equals(user.getLast_name()) : user.getLast_name() != null)
            return false;
        if (getPassword() != null ? !getPassword().equals(user.getPassword()) : user.getPassword() != null)
            return false;
        if (getContactList() != null ? !getContactList().equals(user.getContactList()) : user.getContactList() != null)
            return false;
        if (getChatHistories() != null ? !getChatHistories().equals(user.getChatHistories()) : user.getChatHistories() != null)
            return false;
        return getUserRoles() != null ? getUserRoles().equals(user.getUserRoles()) : user.getUserRoles() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getLogin() != null ? getLogin().hashCode() : 0);
        result = 31 * result + (getFirst_name() != null ? getFirst_name().hashCode() : 0);
        result = 31 * result + (getLast_name() != null ? getLast_name().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getContactList() != null ? getContactList().hashCode() : 0);
        result = 31 * result + (getChatHistories() != null ? getChatHistories().hashCode() : 0);
        result = 31 * result + (getUserRoles() != null ? getUserRoles().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
