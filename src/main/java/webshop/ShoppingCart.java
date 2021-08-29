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

    private List<ShoppingCartPosition> cartList;
    
    /**
     * If there is already a session cookie set, the id can be given as parameter.
     * @param sessID
     */
    public ShoppingCart(){
        this.cartList = new ArrayList<>();
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

}