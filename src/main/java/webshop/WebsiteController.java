package webshop;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lukas Kröker
 * @author Jens Trautmann
 * @author Kasimir Eckhardt
 */
@Controller
public class WebsiteController {

    @Autowired
    JdbcTemplate db;

    @Autowired
    SessionController sessionController;

    /**
     * Homepage
     * 
     * @param sessID
     * @param response
     * @param model
     * @return home.html template
     */
    @GetMapping("/")
    public String home(@CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("email");
        model.addAttribute("templateName", "home");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    @PostMapping("/newsletter")
    public String newsletterRegistration(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, @RequestParam("email") String email, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());

        boolean alreadyExists = db.update("UPDATE newsletter_emails SET email = ?  WHERE email = ?", email, email) > 0;

        if (!alreadyExists) {
            db.update("INSERT INTO newsletter_emails VALUES (?)", email);
            String message = "Guten Tag lieber Bierfreund,\n\ndanke, dass du dich für unser Bier interessierst. Wir halten dich zukünftig immer auf dem Laufenden.\n\nDein Bielefelder Unikat-Team";
            JavaMail.sendMessage(email, email, "Newsletter", message);
        }
        model.addAttribute("alreadyExists", alreadyExists);
        model.addAttribute("templateName", "newsletter");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * Sortiment
     * 
     * @param sessID
     * @param response
     * @param model
     * @return sortiment.html template
     */
    @GetMapping("/sortiment")
    public String sortiment(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        List<Product> products = db.query("SELECT * FROM products WHERE active = TRUE", new ProductRowMapper());
        model.addAttribute("products", products);
        model.addAttribute("templateName", "sortiment");
        model.addAttribute("title", "Sortiment");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * product page
     * 
     * @param productID
     * @param sessID
     * @param response
     * @param model
     * @return product.html template
     */
    @GetMapping("/product")
    public String product(@RequestParam(value = "product", required = true) Integer productID,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        Product product = db.queryForObject("SELECT * FROM products WHERE id = ?", new ProductRowMapper(), productID);
        model.addAttribute(product);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("templateName", "product");
        model.addAttribute("title", product.getName());
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * Impressum
     * 
     * @param sessID
     * @param response
     * @param model
     * @return impressum.html template
     */
    @GetMapping("/impressum")
    public String impressum(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("templateName", "impressum");
        model.addAttribute("title", "Impressum");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * AGB
     * 
     * @param sessID
     * @param response
     * @param model
     * @return agb.html template
     */
    @GetMapping("/agb")
    public String agb(@CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("templateName", "agb");
        model.addAttribute("title", "AGB");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * data safety
     * 
     * @param sessID
     * @param response
     * @param model
     * @return datenschutz.html template
     */
    @GetMapping("/datenschutz")
    public String datenschutz(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("templateName", "datenschutz");
        model.addAttribute("title", "Datenschutz");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * contact-webpage
     * 
     * @param sessID
     * @param response
     * @param model
     * @return contact.html template
     */
    @GetMapping("/contact")
    public String contact(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("templateName", "contact");
        model.addAttribute("title", "Kontakt");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * 
     * @param sessID
     * @param response
     * @param model
     * @return history.html template
     */
    @GetMapping("/history")
    public String history(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("templateName", "history");
        model.addAttribute("title", "Geschichte");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * 
     * @param sessID
     * @param response
     * @param model
     * @return philosophy.html template
     */
    @GetMapping("/philosophy")
    public String philosophy(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("templateName", "philosophy");
        model.addAttribute("title", "Bierphilosophie");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * shows all database tables (this is only for debugging and would be disabled
     * in production)
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

}