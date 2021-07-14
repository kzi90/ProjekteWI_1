package webshop;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShoppingCartController {
    
    @GetMapping(value = "/addProductToCart")
    public String addProductToCart(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
                                   @RequestParam(value = "productID", required = true) Integer productID){
        ShoppingCart shoppingCart = ShoppingCart.findBySessID(Integer.valueOf(sessID));
        shoppingCart.addToCart(productID);
        return "redirect:/";
    }

    @GetMapping("/shoppingCart")
    public String shoppingCart(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
                                                                       HttpServletResponse response,
                                                                                     Model model) {
        ShoppingCart shoppingCart;
        if (sessID.isEmpty()){
            shoppingCart = new ShoppingCart();
            Cookie cookie = new Cookie("SessionID", shoppingCart.getSessID().toString());
            cookie.setMaxAge(-1); // Session cookie
            response.addCookie(cookie);
        } else {
            shoppingCart = ShoppingCart.findBySessID(Integer.valueOf(sessID));
        }
        model.addAttribute(shoppingCart);
        return "shoppingCart";
    }

}