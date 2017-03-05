package hrininlab.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mrhri on 29.01.2017.
 */
@Entity
@Table(name = "chat_history")
public class ChatHistory implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner_id;

    @ManyToOne
    @JoinColumn(name = "which_user_id")
    private User which_user_id;

    @Column(name = "history")
    private String history;

    public ChatHistory() {
    }

    public ChatHistory(User owner_id, User which_user_id, String history) {
        this.owner_id = owner_id;
        this.which_user_id = which_user_id;
        this.history = history;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(User owner_id) {
        this.owner_id = owner_id;
    }

    public User getWhich_user_id() {
        return which_user_id;
    }

    public void setWhich_user_id(User which_user_id) {
        this.which_user_id = which_user_id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
