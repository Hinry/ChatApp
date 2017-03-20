package hrininlab.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mrhri on 29.01.2017.
 */
@Entity
@Table(name = "contact_list")
public class ContactList implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int list_id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(name="allias")
    private String allias;

    public ContactList(User owner_id, User user_id) {
        this.owner_id = owner_id;
        this.user_id = user_id;
    }

    public ContactList() {
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public User getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(User owner_id) {
        this.owner_id = owner_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public String getAllias() {
        return allias;
    }

    public void setAllias(String allias) {
        this.allias = allias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactList)) return false;

        ContactList that = (ContactList) o;

        if (getList_id() != that.getList_id()) return false;
        if (getOwner_id() != null ? !getOwner_id().equals(that.getOwner_id()) : that.getOwner_id() != null)
            return false;
        if (getUser_id() != null ? !getUser_id().equals(that.getUser_id()) : that.getUser_id() != null) return false;
        return getAllias() != null ? getAllias().equals(that.getAllias()) : that.getAllias() == null;
    }


    @Override
    public String toString() {
        return "ContactList{" +
                "list_id=" + list_id +
                ", owner_id=" + owner_id +
                ", user_id=" + user_id +
                ", allias='" + allias + '\'' +
                '}';
    }
}
