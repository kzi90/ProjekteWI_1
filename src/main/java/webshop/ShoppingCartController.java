package webshop;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Kasimir Eckhardt
 */
@Controller
public class ShoppingCartController {

    @Autowired
    JdbcTemplate db;

    @Autowired
    SessionController sessionController;

    /**
     * add product to shoppingcart, productnumber has to be given as parameter, e.g.
     * /addProductToCart?productID=2
     * 
     * @param sessID
     * @param productID
     * @return
     */
    @GetMapping("/addProductToCart")
    public String addProductToCart(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            @RequestParam(value = "productID", required = true) Integer productID,
            @RequestParam(value = "quantity", required = true) Integer quantity, HttpServletResponse response) {
        sessionController.getOrSetSession(sessID, response).getShoppingCart().addToCart(productID, quantity);
        return "redirect:/shoppingcart";
    }

    /**
     * shoppingcart page
     * 
     * @param loggedInUser
     * @param sessID
     * @param response
     * @param model
     * @return shoppingcart.html template
     */
    @GetMapping("/shoppingcart")
    public String shoppingCart(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInUser = session.getLoggedInUser();
        if (!loggedInUser.isEmpty()) {
            // get customer and address data from database
            Customer customer = db.queryForObject("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(),
                    loggedInUser);
            if (customer != null) {
                Address address = db.queryForObject("SELECT * FROM addresses WHERE id = ?", new AddressRowMapper(),
                        customer.getAddressID());
                model.addAttribute(customer);
                if (address != null) {
                    model.addAttribute(address);
                }
            }
        }
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = session.getShoppingCart();
        model.addAttribute("shoppingcart", shoppingCart);
        List<String[]> shoppingCartLines = new ArrayList<>();
        Product product;
        Double lineTotal;
        Double total = 0.0;
        for (ShoppingCartPosition pos : shoppingCart.getCartList()) {
            product = db.queryForObject("SELECT * FROM products WHERE id = ?", new ProductRowMapper(),
                    pos.getProductID());
            lineTotal = Math.round(pos.getQuantity() * product.getPrice() * 100) / 100.0;
            total += lineTotal;
            shoppingCartLines.add(new String[] { pos.getProductID().toString(), product.getType(), product.getName(),
                    product.getImgURL(), pos.getQuantity().toString(), String.format("%.2f", product.getPrice()),
                    String.format("%.2f", lineTotal) });
        }
        model.addAttribute("shoppingCartLines", shoppingCartLines);
        total = Math.round(total * 100) / 100.0;
        model.addAttribute("total", String.format("%.2f", total));
        model.addAttribute("templateName", "shoppingcart");
        model.addAttribute("title", "Warenkorb");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

}