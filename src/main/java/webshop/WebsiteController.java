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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebsiteController {

    @Autowired
    JdbcTemplate db;

    /**
     * 
     * @param model
     * @return home.html (homepage)
     */
    @GetMapping("/")
    public String home(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser, Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        List<Product> products = db.query("SELECT * FROM products", new ProductRowMapper());
        model.addAttribute("products", products);
        ShoppingCart shoppingCart = new ShoppingCart();
        model.addAttribute("shoppingCart", shoppingCart);
        return "home";
    }

    /**
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

    @GetMapping("/cookieset")
    @ResponseBody
    public String cookieset(HttpServletResponse response) {
        Cookie cookie = new Cookie("username", "Kasimir");
        response.addCookie(cookie);
        cookie = new Cookie("CookieTestName", "CookieTestValue");
        response.addCookie(cookie);
        cookie = new Cookie("loggedInUser", "e@mail.ad");
        response.addCookie(cookie);
        return "Set some cookies";
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

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        model.addAttribute("shoppingCart", shoppingCart)
        return "shoppingCart";
    }

}