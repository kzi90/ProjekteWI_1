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
import org.springframework.web.bind.annotation.PostMapping;

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
            Model model) throws DataAccessException, NoSuchAlgorithmException, ParseException {
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
        System.out.println("passwort_reset page wurde aufgerufen");

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
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            HttpServletRequest request, @ModelAttribute("email") String email, Model model)
            throws NoSuchAlgorithmException {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        if (db.query("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(), email).isEmpty()) {
            return "redirect:/login";
        }
        // generates temorary password
        int leftLimit = 99; // letter 'c'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

        System.out.println("Das Passwort lautet: " + generatedString);
        System.out.println("Der zugehörige Hash" + Convert.stringToHash(generatedString));
        db.update("UPDATE customers SET pass_hash = ? WHERE email = ?", Convert.stringToHash(generatedString), email);
        String message = "Guten Tag,\n\n" + "Ihr temporäres Passwort lautet: " + generatedString + "\n"
                + "Bitte ändern Sie nach dem Login das Passwort schnellstmöglich.\n\nLiebe Grüße,\nIhr Bielefelder Unikat-Team";
        JavaMail.sendMessage(email, email, message);
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
        Address address = db.queryForObject("SELECT * FROM addresses WHERE id = ?", new AddressRowMapper(),
                customer.getAddressID());
        model.addAttribute(customer);
        model.addAttribute(address);
        model.addAttribute("newPass", "");

        model.addAttribute("templateName", "account");
        model.addAttribute("title", "Kundendaten");
        return "layout";
    }

    @PostMapping("/account")
    public String account(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            @ModelAttribute Customer customer, @ModelAttribute Address address,
            @ModelAttribute("newPass") String newPass, Model model)
            throws DataAccessException, NoSuchAlgorithmException {
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);

        Customer savedCust = db.queryForObject("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(),
                loggedInUser);

        if (!customer.equalsCust(savedCust)) {
            db.update(
                    "UPDATE customers SET firstname = ?, lastname = ?, birthdate = ?, email = ?, phonenumber = ? WHERE id = ?",
                    customer.getFirstname(), customer.getLastname(), customer.getBirthdate(), customer.getEmail(),
                    customer.getPhonenumber(), savedCust.getId());
            if (!customer.getEmail().equals(savedCust.getEmail())) {
                Cookie cookie = new Cookie("loggedInUser", customer.getEmail());
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        // change password
        if (!newPass.isEmpty()) {
            if (savedCust.login(customer.getPassHash())) { // in PassHash steht temporaer das eingegebene KlartextPW aus
                                                           // dem Formular
                db.update("UPDATE customers SET pass_hash = ? WHERE id = ?", Convert.stringToHash(newPass),
                        savedCust.getId());
            } // else-Zweig, Kunde mitteilen, dass altes PW falsch war
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

        model.addAttribute("loggedInUser", customer.getEmail());
        model.addAttribute("templateName", "account");
        model.addAttribute("title", "Kundendaten");
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
            throws DataAccessException, ParseException, NoSuchAlgorithmException {
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

}