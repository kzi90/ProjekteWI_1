package webshop;

import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebsiteController {

    @Autowired
    JdbcTemplate db;

    /**
     * Homepage
     * 
     * @param loggedInUser
     * @param model
     * @return home.html template
     */
    @GetMapping("/")
    public String home(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "home");
        return "layout";
    }

    /**
     * Sortiment / Bestellseite
     * 
     * @param loggedInUser
     * @param sessID
     * @param response
     * @param model
     * @return sortiment.html template
     */
    @GetMapping("/sortiment")
    public String sortiment(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        List<Product> products = db.query("SELECT * FROM products WHERE active = TRUE", new ProductRowMapper());
        model.addAttribute("products", products);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "sortiment");
        model.addAttribute("title", "Sortiment");
        return "layout";
    }

    /**
     * 
     * Produktseite
     * 
     * @param productID
     * @param loggedInUser
     * @param sessID
     * @param response
     * @param model
     * @return product.html template
     */
    @GetMapping("/product")
    public String product(@RequestParam(value = "product", required = true) Integer productID,
            @CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        Product product = db.queryForObject("SELECT * FROM products WHERE id = ?", new ProductRowMapper(), productID);
        model.addAttribute(product);
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "product");
        model.addAttribute("title", product.getName());
        return "layout";
    }

    /**
     * Impressum
     * 
     * @param loggedInUser
     * @param model
     * @return impressum.html template
     */
    @GetMapping("/impressum")
    public String impressum(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "impressum");
        model.addAttribute("title", "Impressum");
        return "layout";
    }

    /**
     * AGB
     * 
     * @param loggedInUser
     * @param model
     * @return agb.html template
     */
    @GetMapping("/agb")
    public String agb(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "agb");
        model.addAttribute("title", "AGB");
        return "layout";
    }

    /**
     * Datenschutz
     * 
     * @param loggedInUser
     * @param model
     * @return datenschutz.html template
     */
    @GetMapping("/datenschutz")
    public String datenschutz(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "datenschutz");
        model.addAttribute("title", "Datenschutz");
        return "layout";
    }

    /**
     * contact-webpage
     * 
     * @param loggedInUser
     * @param model
     * @return contact.html template
     */
    @GetMapping("/contact")
    public String contact(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "contact");
        model.addAttribute("title", "Kontakt");
        return "layout";
    }

    @GetMapping("/history")
    public String history(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "history");
        model.addAttribute("title", "Geschichte");
        return "layout";
    }

    @GetMapping("/philosophy")
    public String philosophy(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "philosophy");
        model.addAttribute("title", "Bierphilosophie");
        return "layout";
    }

    /**
     * shows all database tables
     * 
     * @param model
     * @return db.html template
     */
    @GetMapping("/db")
    public String db(Model model) {
        List<Address> addresses = db.query("SELECT * FROM addresses", new AddressRowMapper());
        model.addAttribute("addresses", addresses);
        List<Employee> employees = db.query("SELECT * FROM employees", new EmployeeRowMapper());
        model.addAttribute("employees", employees);
        List<Customer> customers = db.query("SELECT * FROM customers", new CustomerRowMapper());
        model.addAttribute("customers", customers);
        List<Product> products = db.query("SELECT * FROM products", new ProductRowMapper());
        model.addAttribute("products", products);
        List<Order> orders = db.query("SELECT * FROM orders", new OrderRowMapper());
        model.addAttribute("orders", orders);
        List<OrderPosition> orderPositions = db.query("SELECT * FROM orderpositions", new OrderPositionRowMapper());
        model.addAttribute("orderPositions", orderPositions);
        return "db";
    }

    @GetMapping("/cookieshow")
    public String cookieshow(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        model.addAttribute("cookies", cookies);
        return "cookieshow";
    }

    @GetMapping("/cookiedel")
    @ResponseBody
    public String cookiedel(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setValue(null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "Deleted all cookies";
    }

}