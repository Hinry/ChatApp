package hrininlab.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrhri on 05.03.2017.
 */
public class UsersOnlineListWrapper {

    private List users = new ArrayList();


    public List getPersons() {
        return users;
    }
    public void setPersons(List persons) {
        this.users = persons;
    }
}
