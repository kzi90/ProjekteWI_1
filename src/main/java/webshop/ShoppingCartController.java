package webshop;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShoppingCartController {

    @Autowired
    JdbcTemplate db;
    
    @GetMapping("/addProductToCart")
    public String addProductToCart(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
                                   @RequestParam(value = "productID", required = true) Integer productID){
        ShoppingCart shoppingCart = ShoppingCart.findBySessID(Integer.valueOf(sessID));
        shoppingCart.addToCart(productID);
        return "redirect:/sortiment";
    }

    @GetMapping("/shoppingcart")
    public String shoppingCart(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
                                  @CookieValue(value = "SessionID", defaultValue = "") String sessID,
                                                                          HttpServletResponse response,
                                                                                        Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart;
        if (sessID.isEmpty()){
            shoppingCart = new ShoppingCart();
            Cookie cookie = new Cookie("SessionID", shoppingCart.getSessID().toString());
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            shoppingCart = ShoppingCart.findBySessID(Integer.valueOf(sessID));
        }
        List<String[]> shoppingCartLines = new ArrayList<>();
        Product product;
        for (ShoppingCartPosition pos : shoppingCart.getCartList()) {
            product = db.queryForObject("SELECT * FROM products WHERE id = ?", new ProductRowMapper(), pos.getProductID());
            shoppingCartLines.add(new String[] {pos.getProductID().toString(), product.getName(), product.getImgURL(), pos.getQuantity().toString()});
        }
        model.addAttribute("shoppingCartLines", shoppingCartLines);
        return "shoppingcart";
    }

}