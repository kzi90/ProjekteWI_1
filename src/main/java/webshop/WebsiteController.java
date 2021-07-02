package webshop;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebsiteController {

    @Autowired
    JdbcTemplate db;
    
    /**
     * 
     * @param model
     * @return home.html template
     */
    @GetMapping("/")
    public String home(Model model){
        List<Customer> customers = db.query("SELECT * FROM customers", new CustomerRowMapper());
        model.addAttribute("customers", customers);
        List<Address> addresses = db.query("SELECT * FROM addresses", new AddressRowMapper());
        model.addAttribute("addresses", addresses);
        List<Product> products = db.query("SELECT * FROM products", new ProductRowMapper());
        model.addAttribute("products", products);
        return "home";
    }
}