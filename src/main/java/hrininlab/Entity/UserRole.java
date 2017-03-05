package hrininlab.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mrhri on 29.01.2017.
 */
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name="TYPE", length=15, unique=true, nullable=false)
    private String type = UserRoleType.USER.getUserProfileType();

    public UserRole() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;

        UserRole userRole = (UserRole) o;

        if (getId() != userRole.getId()) return false;
        return getType() != null ? getType().equals(userRole.getType()) : userRole.getType() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "type='" + type + '\'' +
                '}';
    }
}
