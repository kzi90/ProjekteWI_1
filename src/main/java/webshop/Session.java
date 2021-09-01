package webshop;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Session {
    private String id;
    private Boolean cookiesAccepted;
    private String loggedInUser;
    private String loggedInEmp;
    private ShoppingCart shoppingCart;

    public Session(String id){
        this.id = id;
        this.cookiesAccepted = false;
        this.loggedInUser = "";
        this.loggedInEmp = "";
        this.shoppingCart = new ShoppingCart();
    }
}