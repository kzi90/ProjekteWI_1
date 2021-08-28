package webshop;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Kasimir Eckhardt
 */
@Getter @Setter
public class ShoppingCart {
    
    protected static final List<ShoppingCart> shoppingCarts = new ArrayList<>();

    private String sessID;
    private List<ShoppingCartPosition> cartList;
    
    /**
     * If there is already a session cookie set, the id can be given as parameter.
     * @param sessID
     */
    public ShoppingCart(String sessID){
        this.sessID = sessID;
        this.cartList = new ArrayList<>();
        shoppingCarts.add(this);
    }

    /**
     * Add product to shoppingcart
     * @param productID
     */
    public void addToCart(Integer productID, Integer quantity) {
        for (ShoppingCartPosition shoppingCartPosition : this.cartList) {
            if (shoppingCartPosition.getProductID().equals(productID)) {
                shoppingCartPosition.setQuantity(shoppingCartPosition.getQuantity() + quantity);
                if (shoppingCartPosition.getQuantity() <= 0){
                    this.cartList.remove(shoppingCartPosition);
                }
                return;
            }
        }
        cartList.add(new ShoppingCartPosition(productID, quantity));
    }

    /**
     * 
     * @return amount of bottles in shoppingcart
     */
    public Integer getAmountOfBottles(){
        Integer amount = 0;
        for (ShoppingCartPosition pos : cartList) {
            amount += pos.getQuantity();
        }
        return amount;
    }

    /**
     * Find shopping cart from static list by session id.
     * @param sessID
     * @return the shoppingcart with the given session id.
     */
    public static ShoppingCart findBySessID(String sessID){
        for (ShoppingCart shoppingCart : shoppingCarts) {
            if (shoppingCart.getSessID().equals(sessID)){
                return shoppingCart;
            }
        }
        shoppingCarts.add(new ShoppingCart(sessID));
        return findBySessID(sessID);
    }

}