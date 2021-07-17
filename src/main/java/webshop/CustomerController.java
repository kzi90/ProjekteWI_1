package webshop;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
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
     * register-page
     * @param loggedInUser
     * @param model
     * @return register.html template
     */
    @GetMapping("/register")
    public String register(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser, Model model){
        model.addAttribute("loggedInUser", loggedInUser);
        Customer customer = new Customer();
        model.addAttribute(customer);
        Address address = new Address();
        model.addAttribute(address);
        return "register";
    }

    /**
     * Answer page after submitting registration values
     * @param customer built automatically with submitted values
     * @param address built automatically with submitted values
     * @param loggedInUser read from cookie
     * @param model saves objects as attributes
     * @return registered.html template
     * @throws DataAccessException
     * @throws ParseException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/register")
    public String registered(@ModelAttribute Customer customer,
                             @ModelAttribute Address address,
                             @CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
                                                                                      Model model) throws DataAccessException,
                                                                                                          ParseException,
                                                                                                          NoSuchAlgorithmException{
        model.addAttribute("loggedInUser", loggedInUser);
        boolean emailAlreadyRegistered = (!saveCustWithAddress(customer, address));
        model.addAttribute("emailAlreadyRegistered", emailAlreadyRegistered);
        return "registered";
    }

    /**
     * save customer- and addressdata if not already existing in database
     * @param customer
     * @param address
     * @return true if the email of the customer was not used before, else false
     * @throws DataAccessException
     * @throws ParseException
     * @throws NoSuchAlgorithmException
     */
    public boolean saveCustWithAddress(Customer customer, Address address) throws DataAccessException,
                                                                                  ParseException,
                                                                                  NoSuchAlgorithmException{
        String testSQL = "SELECT * FROM customers WHERE email = ?;";
        boolean emailNotRegisteredBefore = db.query(testSQL, new CustomerRowMapper(), customer.getEmail()).isEmpty();
        if (emailNotRegisteredBefore){
            address = addressController.saveAddress(address);
            // database generates id automatically
            String saveSQL = "INSERT INTO customers (firstname, lastname, birthdate, " +
                             "address_id, email, phonenumber, pass_hash) VALUES (?, ?, ?, ?, ?, ?, ?);";
            this.db.update(saveSQL, customer.getFirstname(),
                                    customer.getLastname(),
                                    // database works correct with String in this format, but not with type Date.
                                    new SimpleDateFormat("yyyy-MM-dd").format(
                                        new SimpleDateFormat("dd.MM.yyyy").parse(customer.getBirthdate())),
                                    address.getId(),
                                    customer.getEmail(),
                                    customer.getPhonenumber(),
                                    // customer.passHash contains plain text password right now
                                    Convert.stringToHash(customer.getPassHash()));
        }
        return emailNotRegisteredBefore;
    }

}