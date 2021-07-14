package webshop;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // needed for getting form-inputs via POST and thymeleaf
@NoArgsConstructor
public class ShoppingCart {
    // Liste von Integer Paaren productID und Menge
    private List<ShoppingCartPosition> cartList;

    public void addToCart(Integer productID) {
        for (ShoppingCartPosition shoppingCartPosition : cartList) {
            if (shoppingCartPosition.getProductID().equals(productID)) {
                shoppingCartPosition.setQuantity(shoppingCartPosition.getQuantity() + 1);
                return;
            }
        }
        cartList.add(new ShoppingCartPosition(productID, 1));
    }
}
