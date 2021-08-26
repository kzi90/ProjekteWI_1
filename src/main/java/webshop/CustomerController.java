package webshop;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Random;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Kasimir Eckhardt
 */
@Controller
public class CustomerController {

    @Autowired
    private JdbcTemplate db;

    @Autowired
    private AddressController addressController;

    /**
     * register-page
     * 
     * @param loggedInUser
     * @param model
     * @return register.html template
     */
    @GetMapping("/register")
    public String register(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        Customer customer = new Customer();
        model.addAttribute(customer);
        Address address = new Address();
        model.addAttribute(address);
        model.addAttribute("templateName", "register");
        model.addAttribute("title", "Registrieren");
        return "layout-neutral";
    }

    /**
     * Answer page after submitting registration values
     * 
     * @param customer     built automatically with submitted values
     * @param address      built automatically with submitted values
     * @param loggedInUser read from cookie
     * @param model        saves objects as attributes
     * @return registered.html template
     * @throws DataAccessException
     * @throws ParseException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/register")
    public String registered(@ModelAttribute Customer customer, @ModelAttribute Address address,
            @CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) throws DataAccessException, NoSuchAlgorithmException {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        boolean emailAlreadyRegistered = (!saveCustWithAddress(customer, address));
        model.addAttribute("emailAlreadyRegistered", emailAlreadyRegistered);
        model.addAttribute("templateName", "registered");
        return "layout";
    }

    /**
     * login form
     * 
     * @param loggedInUser
     * @param model
     * @return login.html template
     */
    @GetMapping("/login")
    public String login(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("cameFrom", request.getHeader("Referer"));
        Customer customer = new Customer();
        model.addAttribute(customer);
        model.addAttribute("templateName", "login");
        return "layout-neutral";
    }

    /**
     * trying login with submitted data
     * 
     * @param customer
     * @param response
     * @param model
     * @return redirects to /sortiment if login successful, else to /loginfail
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/login")
    public String loggedin(@ModelAttribute Customer customer, @ModelAttribute("cameFrom") String cameFrom,
            HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {
        List<Customer> customers = db.query("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(),
                customer.getEmail());
        // customer.passHash contains the plain text password right now!
        if (!customers.isEmpty() && customers.get(0).login(customer.getPassHash())) {
            Cookie cookie = new Cookie("loggedInUser", customer.getEmail());
            response.addCookie(cookie);
            return cameFrom.endsWith("/logout") || cameFrom.endsWith("/register") || cameFrom.endsWith("/loginfail")
                    || cameFrom.endsWith("/password_reset") ? "redirect:/sortiment" : "redirect:" + cameFrom;
        } else {
            return "redirect:/loginfail";
        }
    }

    /**
     * tell the user that his login-attempt failed
     * 
     * @param loggedInUser
     * @param model
     * @return loginfail.html template
     */
    @GetMapping("/loginfail")
    public String loginfail(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("templateName", "loginfail");
        return "layout";
    }

    /**
     * logout
     * 
     * @param response
     * @param model
     * @return logout.html template
     */
    @GetMapping("/logout")
    public String logout(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        Cookie cookie = new Cookie("loggedInUser", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        model.addAttribute("loggedInUser", "");
        model.addAttribute("templateName", "logout");
        return "layout";
    }

    /**
     * reset page
     * 
     * @param loggedInUser
     * @param model
     * @return password_reset.html template
     */
    @GetMapping("/password_reset")
    public String passwordReset(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("email");
        model.addAttribute("templateName", "password_reset");
        model.addAttribute("title", "Passwort zurücksetzen");
        return "layout-neutral";
    }

    /**
     * reset page
     * 
     * @param loggedInUser
     * @param model
     * @return password_reset.html template
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/password_reset")
    public String passwordResetet(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @ModelAttribute("email") String email) throws NoSuchAlgorithmException {
        if (!loggedInUser.isEmpty()) {
            return "redirect:/";
        }
        List<Customer> customers = db.query("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(), email);
        if (customers.isEmpty()) {
            return "redirect:/login";
        }
        // generates temorary password
        int leftLimit = 99; // letter 'c'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        db.update("UPDATE customers SET pass_hash = ? WHERE email = ?", Convert.stringToHash(generatedString), email);
        String fullname = customers.get(0).getFirstname() + " " + customers.get(0).getLastname();
        String message = "Guten Tag " + fullname + ",\n\nIhr temporäres Passwort lautet: " + generatedString + "\n"
                + "Bitte ändern Sie nach dem Login das Passwort schnellstmöglich.\n\nLiebe Grüße,\nIhr Bielefelder Unikat-Team";
        JavaMail.sendMessage(email, fullname, "Passwort zurücksetzen", message);
        return "redirect:/login";
    }

    @GetMapping("/account")
    public String account(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        if (loggedInUser.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);

        // get customer and address data from database
        Customer customer = db.queryForObject("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(),
                loggedInUser);
        Address address;
        if (customer != null){
            address = db.queryForObject("SELECT * FROM addresses WHERE id = ?", new AddressRowMapper(),
                customer.getAddressID());
            model.addAttribute(customer);
            if (address != null){
                model.addAttribute(address);
            }
        }
        model.addAttribute("newPass", "");
        model.addAttribute("templateName", "account");
        model.addAttribute("title", "Kundendaten");
        return "layout";
    }

    @PostMapping("/account")
    public String account(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            HttpServletResponse response, @ModelAttribute Customer customer, @ModelAttribute Address address,
            @ModelAttribute("newPass") String newPass) throws DataAccessException, NoSuchAlgorithmException {

        Customer savedCust = db.queryForObject("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(),
                loggedInUser);

        // update changed data in database
        if (!customer.getFirstname().equals(savedCust.getFirstname())) {
            db.update("UPDATE customers SET firstname = ? WHERE id = ?", customer.getFirstname(), savedCust.getId());
        }
        if (!customer.getLastname().equals(savedCust.getLastname())) {
            db.update("UPDATE customers SET lastname = ? WHERE id = ?", customer.getLastname(), savedCust.getId());
        }
        if (!customer.getBirthdate().equals(savedCust.getBirthdate())) {
            db.update("UPDATE customers SET birthdate = ? WHERE id = ?", customer.getBirthdate(), savedCust.getId());
        }
        if (!customer.getPhonenumber().equals(savedCust.getPhonenumber())) {
            db.update("UPDATE customers SET phonenumber = ? WHERE id = ?", customer.getPhonenumber(),
                    savedCust.getId());
        }
        // email is only changed if there isn't another account with this email yet
        if (!customer.getEmail().equals(savedCust.getEmail())
                && db.query("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(), customer.getEmail())
                        .isEmpty()) {
            db.update("UPDATE customers SET email = ? WHERE id = ?", customer.getEmail(), savedCust.getId());
            Cookie cookie = new Cookie("loggedInUser", customer.getEmail());
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        // change password
        if (!newPass.isEmpty() && savedCust.login(customer.getPassHash())) {
            // in PassHash steht temporaer das eingegebene KlartextPW
            db.update("UPDATE customers SET pass_hash = ? WHERE id = ?", Convert.stringToHash(newPass),
                    savedCust.getId());
        }

        Address savedAddress = db.queryForObject("SELECT * FROM addresses WHERE id = ?", new AddressRowMapper(),
                savedCust.getAddressID());

        if (!address.equalsAddress(savedAddress)) {
            address = addressController.saveAddress(address);
            db.update("UPDATE customers SET address_id = ? WHERE id = ?", address.getId(), savedCust.getId());
            if (db.query("SELECT * FROM customers WHERE address_id = ?", new CustomerRowMapper(),
                    savedCust.getAddressID()).isEmpty()
                    && db.query("SELECT * FROM employees WHERE address_id = ?", new EmployeeRowMapper(),
                            savedCust.getAddressID()).isEmpty()) {
                db.update("DELETE FROM addresses WHERE id = ?", savedCust.getAddressID());
            }
        }
        return "redirect:/account";
    }

    @GetMapping("/deluser")
    public String deluser(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        if (loggedInUser.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("loggedInUser", "");
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);

        // set customer inactive
        db.update("UPDATE customers SET active = FALSE WHERE email = ?", loggedInUser);

        // delete cookie
        Cookie cookie = new Cookie("loggedInUser", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        model.addAttribute("templateName", "deluser");
        return "layout";
    }

    /**
     * save customer- and addressdata if not already existing in database
     * 
     * @param customer
     * @param address
     * @return true if the email of the customer was not used before, else false
     * @throws DataAccessException
     * @throws ParseException
     * @throws NoSuchAlgorithmException
     */
    public boolean saveCustWithAddress(Customer customer, Address address)
            throws DataAccessException, NoSuchAlgorithmException {
        String testSQL = "SELECT * FROM customers WHERE email = ?;";
        boolean emailNotRegisteredBefore = db.query(testSQL, new CustomerRowMapper(), customer.getEmail()).isEmpty();
        if (emailNotRegisteredBefore) {
            address = addressController.saveAddress(address);
            // database generates id automatically
            String saveSQL = "INSERT INTO customers (firstname, lastname, birthdate, "
                    + "address_id, email, phonenumber, pass_hash) VALUES (?, ?, ?, ?, ?, ?, ?);";
            db.update(saveSQL, customer.getFirstname(), customer.getLastname(), customer.getBirthdate(),
                    address.getId(), customer.getEmail(), customer.getPhonenumber(),
                    // customer.passHash contains plain text password right now
                    Convert.stringToHash(customer.getPassHash()));
        }
        return emailNotRegisteredBefore;
    }

    /**
     * 
     * @param loggedInUser
     * @param sessID
     * @param response
     * @param model
     * @return customer_search.html template
     */
    @GetMapping("/customer_search")
    public String customerSearch(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        // TODO check mitarbeiter login
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        model.addAttribute("email");
        model.addAttribute("templateName", "customer_search");
        model.addAttribute("title", "Benutzer suchen");
        return "layout";
    }

    /**
     * reset page
     * 
     * @param loggedInUser
     * @param model
     * @return redirect to customer_edit.html
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/customer_search")
    public String customerSearched(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            HttpServletRequest request, @ModelAttribute("email") String email, Model model) {
        // TODO check mitarbeiter login
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        List<Customer> customers = db.query("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(), email);
        if (customers.isEmpty()) {
            return "redirect:/customer_search";
        }

        return "redirect:/customer_edit" + customers.get(0).getId().toString();
    }

    /**
     * reset page
     * 
     * @param loggedInUser
     * @param model
     * @return customer_edit.html template
     */
    @GetMapping("/customer_edit{id}")
    public String customerEdit(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            @PathVariable String id, Model model) {
        // TODO check mitarbeiter login
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        Customer customer = db.queryForObject("SELECT * FROM customers WHERE id = ?", new CustomerRowMapper(), id);
        Address address = db.queryForObject("SELECT * FROM addresses WHERE id = ?", new AddressRowMapper(),
                customer.getAddressID());
        model.addAttribute(address);
        model.addAttribute(customer);
        model.addAttribute("templateName", "customer_edit");
        model.addAttribute("title", "Benutzer ändern");
        return "layout";
    }

    @PostMapping("/customer_edit{id}")
    public String customerEdited(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            HttpServletResponse response, @ModelAttribute Customer customer, @ModelAttribute Address address,
            @PathVariable String id, @ModelAttribute("newPass") String newPass)
            throws DataAccessException, NoSuchAlgorithmException {
        // TODO check mitarbeiter login
        Customer savedCust = db.queryForObject("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(),
                customer.getEmail());

        // update changed data in database
        if (!customer.getFirstname().equals(savedCust.getFirstname())) {
            db.update("UPDATE customers SET firstname = ? WHERE id = ?", customer.getFirstname(), savedCust.getId());
        }
        if (!customer.getLastname().equals(savedCust.getLastname())) {
            db.update("UPDATE customers SET lastname = ? WHERE id = ?", customer.getLastname(), savedCust.getId());
        }
        if (!customer.getBirthdate().equals(savedCust.getBirthdate())) {
            db.update("UPDATE customers SET birthdate = ? WHERE id = ?", customer.getBirthdate(), savedCust.getId());
        }
        if (!customer.getPhonenumber().equals(savedCust.getPhonenumber())) {
            db.update("UPDATE customers SET phonenumber = ? WHERE id = ?", customer.getPhonenumber(),
                    savedCust.getId());
        }
        // email is only changed if there isn't another account with this email yet
        if (!customer.getEmail().equals(savedCust.getEmail())
                && db.query("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(), customer.getEmail())
                        .isEmpty()) {
            db.update("UPDATE customers SET email = ? WHERE id = ?", customer.getEmail(), savedCust.getId());
            Cookie cookie = new Cookie("loggedInUser", customer.getEmail());
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        // change password
        if (!newPass.isEmpty()) {
            db.update("UPDATE customers SET pass_hash = ? WHERE id = ?", Convert.stringToHash(newPass),
                    savedCust.getId());
        }

        Address savedAddress = db.queryForObject("SELECT * FROM addresses WHERE id = ?", new AddressRowMapper(),
                savedCust.getAddressID());

        if (!address.equalsAddress(savedAddress)) {
            address = addressController.saveAddress(address);
            db.update("UPDATE customers SET address_id = ? WHERE id = ?", address.getId(), savedCust.getId());
            if (db.query("SELECT * FROM customers WHERE address_id = ?", new CustomerRowMapper(),
                    savedCust.getAddressID()).isEmpty()
                    && db.query("SELECT * FROM employees WHERE address_id = ?", new EmployeeRowMapper(),
                            savedCust.getAddressID()).isEmpty()) {
                db.update("DELETE FROM addresses WHERE id = ?", savedCust.getAddressID());
            }
        }

        // logging messages for debugging
        System.out.println(String.format("neu: %s", customer));
        System.out.println(String.format("alt: %s", savedCust));
        System.out.println(String.format("neu: %s", address));
        System.out.println(String.format("alt: %s", savedAddress));

        return "redirect:/customer_edit" + customer.getId().toString();
    }
}