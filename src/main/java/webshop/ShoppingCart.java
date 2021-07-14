package webshop;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCart {
    
    protected static final List<ShoppingCart> shoppingCarts = new ArrayList<>();

    private Integer sessID;
    private List<ShoppingCartPosition> cartList;

    public ShoppingCart(){
        Integer maxSessID = 1;
        for (ShoppingCart shoppingCart : shoppingCarts) {
            if (shoppingCart.getSessID() > maxSessID){
                maxSessID = shoppingCart.getSessID();
            }
        }
        this.sessID = shoppingCarts.isEmpty() ? maxSessID : maxSessID + 1;
        this.cartList = new ArrayList<>();
        shoppingCarts.add(this);
    }

    /**
     * If there is already a session cookie set, the id can be given as parameter.
     * @param sessID
     */
    public ShoppingCart(Integer sessID){
        this.sessID = sessID;
        this.cartList = new ArrayList<>();
        shoppingCarts.add(this);
    }

    /**
     * Add product to shoppingcart
     * @param productID
     */
    public void addToCart(Integer productID) {
        for (ShoppingCartPosition shoppingCartPosition : cartList) {
            if (shoppingCartPosition.getProductID().equals(productID)) {
                shoppingCartPosition.setQuantity(shoppingCartPosition.getQuantity() + 1);
                return;
            }
        }
        cartList.add(new ShoppingCartPosition(productID, 1));
    }

    /**
     * Find shopping cart from static list by session id.
     * @param sessID
     * @return the shoppingcart with the given session id.
     */
    public static ShoppingCart findBySessID(Integer sessID){
        for (ShoppingCart shoppingCart : shoppingCarts) {
            if (shoppingCart.getSessID().equals(sessID)){
                return shoppingCart;
            }
        }
        shoppingCarts.add(new ShoppingCart(sessID));
        return findBySessID(sessID);
    }

}