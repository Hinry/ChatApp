package hrininlab.Entity;

import java.io.Serializable;

/**
 * Created by mrhri on 07.02.2017.
 */
public enum UserRoleType implements Serializable{

    USER("USER"),
    DBA("DBA"),
    ADMIN("ADMIN");

    String userRoleType;

    private UserRoleType(String userProfileType){
        this.userRoleType = userProfileType;
    }

    public String getUserProfileType(){
        return userRoleType;
    }

}
