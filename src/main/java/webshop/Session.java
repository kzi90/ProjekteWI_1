package webshop;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Session {
    private String id;
    private String loggedInUser;
    private String loggedInEmp;

    public Session(String id){
        this.id = id;
        this.loggedInUser = "";
        this.loggedInEmp = "";
    }
}