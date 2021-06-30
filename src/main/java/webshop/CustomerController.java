package webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomerController {

    @Autowired
    private JdbcTemplate db;

    @Autowired
    private AddressController addressController;

    /**
     * 
     * @param model
     * @return register.html template
     */
    @GetMapping("/register")
    public String register(Model model){
        Customer customer = new Customer();
        model.addAttribute(customer);
        Address address = new Address();
        model.addAttribute(address);
        return "register";
    }

    /**
     * writes the registered customer into the database and shows success message
     * @param person built automatically with submitted values
     * @param model
     * @return registered.html template
     */
    @PostMapping("/register")
    public String registered(@ModelAttribute Customer customer, @ModelAttribute Address address, Model model){
        String testSQL = "SELECT * FROM customers WHERE email = ?;";
        boolean emailAlreadyRegistered = (!db.query(testSQL, new CustomerRowMapper(), customer.getEmail()).isEmpty());
        model.addAttribute("emailAlreadyRegistered", emailAlreadyRegistered);
        if (!emailAlreadyRegistered){
            address = addressController.saveAddress(address);
            // id wird automatisch durch die Datenbank vergeben
            String saveSQL = "INSERT INTO customers (firstname,lastname, address_id, email, phonenumber, pass_hash) VALUES (?, ?, ?, ?, ?, ?);";
            this.db.update(saveSQL, customer.getFirstname(),
                                    customer.getLastname(),
                                    address.getId(),
                                    customer.getEmail(),
                                    customer.getPhonenumber(),
                                    customer.getPassHash());
        }
        return "registered";
    }

}