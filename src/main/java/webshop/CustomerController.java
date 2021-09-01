package webshop;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Random;
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

    @Autowired
    private SessionController sessionController;

    /**
     * accept cookies
     * @param sessID
     * @param request
     * @param response
     * @return redirect to same page
     */
    @GetMapping("/accept_cookies")
    public String acceptCookies(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletRequest request, HttpServletResponse response) {
        Session session = sessionController.getOrSetSession(sessID, response);
        session.setCookiesAccepted(true);
        return "redirect:" + request.getHeader("Referer");
    }

    /**
     * register page
     * 
     * @param response
     * @param model
     * @return register.html template
     */
    @GetMapping("/register")
    public String register(HttpServletResponse response, Model model) {
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
     * @param customer built automatically with submitted values
     * @param address  built automatically with submitted values
     * @param sessID   read from cookie
     * @param response
     * @param model    saves objects as attributes
     * @return
     * @throws DataAccessException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/register")
    public String registered(@ModelAttribute Customer customer, @ModelAttribute Address address,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            HttpServletRequest request, Model model) throws DataAccessException, NoSuchAlgorithmException {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        boolean emailAlreadyRegistered = (!saveCustWithAddress(customer, address));
        if (!emailAlreadyRegistered) {

            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            String validationHash = new Random().ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(10)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            db.update("UPDATE customers SET validation_hash = ? WHERE email = ?;", validationHash, customer.getEmail());

            String fullname = customer.getFirstname() + " " + customer.getLastname();
            String message = "Guten Tag " + fullname + ",\n\ndanke für deine Registrierung bei Bielefelder Unikat. "
                    + "Um deine E-Mail-Adresse zu validieren, öffne bitte den folgenden Link:\n"
                    + request.getServerName() + "/validate" + validationHash
                    + "\nNach der Validierung kannst du dich mit deinem Benutzerkonto anmelden.\n"
                    + "Solltest du dich nicht bei Bielefelder Unikat registriert haben, kannst du diese E-Mail ignorieren.\n\n"
                    + "Freundliche Grüße\n\nDein Bielefelder Unikat Team";
            JavaMail.sendMessage(customer.getEmail(), fullname, "E-Mail-Validierung", message);
        }
        model.addAttribute("emailAlreadyRegistered", emailAlreadyRegistered);
        model.addAttribute("templateName", "registered");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * login form
     * 
     * @param request
     * @param response
     * @param model
     * @return login.html template
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
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
     * @param cameFrom
     * @param sessID
     * @param response
     * @return redirects to /sortiment if login successful, else to /loginfail
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/login")
    public String loggedin(@ModelAttribute Customer customer, @ModelAttribute("cameFrom") String cameFrom,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response)
            throws NoSuchAlgorithmException {
        List<Customer> customers = db.query("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(),
                customer.getEmail());
        // customer.passHash contains the plain text password right now!
        if (!customers.isEmpty() && customers.get(0).login(customer.getPassHash())) {
            sessionController.getOrSetSession(sessID, response).setLoggedInUser(customer.getEmail());
            return cameFrom.endsWith("/logout") || cameFrom.endsWith("/register") || cameFrom.endsWith("/loginfail")
                    || cameFrom.endsWith("/password_reset") || cameFrom.contains("validate") ? "redirect:/sortiment"
                            : "redirect:" + cameFrom;
        } else {
            return "redirect:/loginfail";
        }
    }

    /**
     * tell the user that his login-attempt failed
     * 
     * @param sessID
     * @param response
     * @param model
     * @return loginfail.html template
     */
    @GetMapping("/loginfail")
    public String loginfail(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("templateName", "loginfail");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * logout
     * 
     * @param sessID
     * @param response
     * @param model
     * @return logout.html template
     */
    @GetMapping("/logout")
    public String logout(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("shoppingcart", session.getShoppingCart());
        session.setLoggedInUser("");
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("templateName", "logout");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * reset passwort page
     * 
     * @param response
     * @param model
     * @return password_reset.html template
     */
    @GetMapping("/password_reset")
    public String passwordReset(HttpServletResponse response, Model model) {
        model.addAttribute("email");
        model.addAttribute("templateName", "password_reset");
        model.addAttribute("title", "Passwort zurücksetzen");
        return "layout-neutral";
    }

    /**
     * reset passwort
     * 
     * @param sessID
     * @param response
     * @param email
     * @return
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/password_reset")
    public String passwordResetet(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, @ModelAttribute("email") String email) throws NoSuchAlgorithmException {
        String loggedInUser = sessionController.getOrSetSession(sessID, response).getLoggedInUser();
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
        String message = "Guten Tag " + fullname + ",\n\ndein temporäres Passwort lautet: " + generatedString + "\n"
                + "Bitte ändere nach dem Login das Passwort schnellstmöglich.\n\nLiebe Grüße,\nDein Bielefelder Unikat-Team";
        JavaMail.sendMessage(email, fullname, "Passwort zurücksetzen", message);
        return "redirect:/login";
    }

    /**
     * change customer data by customer
     * 
     * @param sessID
     * @param response
     * @param model
     * @return account.html template
     */
    @GetMapping("/account")
    public String account(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInUser = session.getLoggedInUser();
        if (loggedInUser.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("shoppingcart", session.getShoppingCart());

        // get customer and address data from database
        Customer customer = db.queryForObject("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(),
                loggedInUser);
        Address address;
        if (customer != null) {
            address = db.queryForObject("SELECT * FROM addresses WHERE id = ?", new AddressRowMapper(),
                    customer.getAddressID());
            model.addAttribute(customer);
            if (address != null) {
                model.addAttribute(address);
            }
        }
        model.addAttribute("newPass", "");
        model.addAttribute("templateName", "account");
        model.addAttribute("title", "Kundendaten");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * change customer data by customer
     * 
     * @param sessID
     * @param response
     * @param customer
     * @param address
     * @param newPass
     * @return redirect to /account
     * @throws DataAccessException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/account")
    public String account(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletRequest request, HttpServletResponse response, @ModelAttribute Customer customer,
            @ModelAttribute Address address, @ModelAttribute("newPass") String newPass, Model model)
            throws DataAccessException, NoSuchAlgorithmException {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInUser = session.getLoggedInUser();
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

        // email is only changed if there isn't another account with this email yet
        if (!customer.getEmail().equals(savedCust.getEmail())
                && db.query("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(), customer.getEmail())
                        .isEmpty()) {
            db.update("UPDATE customers SET email = ? WHERE id = ?", customer.getEmail(), savedCust.getId());
            session.setLoggedInUser("");
            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            String validationHash = new Random().ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(10)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            db.update("UPDATE customers SET active = FALSE, validation_hash = ? WHERE email = ?;", validationHash,
                    customer.getEmail());

            String fullname = customer.getFirstname() + " " + customer.getLastname();
            String message = "Guten Tag " + fullname
                    + ",\n\naufgrund der Änderung deiner E-Mail-Adresse ist eine erneute "
                    + "Validierung erforderlich, öffne dazu bitte den folgenden Link:\n" + request.getServerName()
                    + "/validate" + validationHash
                    + "\nNach der Validierung kannst du dich wie gewohnt mit deinem Benutzerkonto anmelden.\n"
                    + "Solltest du kein Benutzerkonto bei Bielefelder Unikat haben, kannst du diese E-Mail ignorieren.\n\n"
                    + "Freundliche Grüße\n\nDein Bielefelder Unikat Team";
            JavaMail.sendMessage(customer.getEmail(), fullname, "E-Mail-Validierung", message);
            model.addAttribute("loggedInUser", "");
            model.addAttribute("shoppingcart", session.getShoppingCart());
            model.addAttribute("templateName", "changed_email");
            model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
            return "layout";
        }

        return "redirect:/account";
    }

    /**
     * deactivate user-account by customer
     * 
     * @param sessID
     * @param response
     * @param model
     * @return deluser.html template
     */
    @GetMapping("/deluser")
    public String deluser(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInUser = session.getLoggedInUser();
        if (loggedInUser.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("shoppingcart", session.getShoppingCart());

        // set customer inactive
        db.update("UPDATE customers SET active = FALSE WHERE email = ?", loggedInUser);

        // logout
        session.setLoggedInUser("");
        model.addAttribute("loggedInUser", session.getLoggedInUser());

        model.addAttribute("templateName", "deluser");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
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
     * validate email after registration
     * 
     * @param sessID
     * @param response
     * @param hash
     * @param model
     * @return validate.html template
     */
    @GetMapping("/validate{hash}")
    public String validate(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, @PathVariable String hash, Model model) {
        if (hash.isEmpty()) {
            return "redirect:/";
        }
        Session session = sessionController.getOrSetSession(sessID, response);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        boolean validated = db
                .update("UPDATE customers SET active = TRUE, validation_hash = '' WHERE validation_hash = ?", hash) > 0;
        model.addAttribute("validated", validated);
        model.addAttribute("templateName", "validate");
        model.addAttribute("title", "E-Mail-Validierung");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * 
     * @param sessID
     * @param response
     * @param model
     * @return customer_search.html template
     */
    @GetMapping("/customer_search")
    public String customerSearch(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInEmp = session.getLoggedInEmp();
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("loggedInEmp", loggedInEmp);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        model.addAttribute("email");
        model.addAttribute("templateName", "customer_search");
        model.addAttribute("title", "Benutzer suchen");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * 
     * @param sessID
     * @param response
     * @param request
     * @param email
     * @param model
     * @return redirect to customer_edit.html
     */
    @PostMapping("/customer_search")
    public String customerSearched(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, HttpServletRequest request, @ModelAttribute("email") String email,
            Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInEmp = session.getLoggedInEmp();
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        List<Customer> customers = db.query("SELECT * FROM customers WHERE email = ?", new CustomerRowMapper(), email);
        if (customers.isEmpty()) {
            return "redirect:/customer_search";
        }
        return "redirect:/customer_edit" + customers.get(0).getId().toString();
    }

    /**
     * 
     * @param sessID
     * @param response
     * @param id
     * @param model
     * @return customer_edit.html template
     */
    @GetMapping("/customer_edit{id}")
    public String customerEdit(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, @PathVariable String id, Model model) {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInEmp = session.getLoggedInEmp();
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("loggedInEmp", loggedInEmp);
        model.addAttribute("loggedInUser", session.getLoggedInUser());
        model.addAttribute("shoppingcart", session.getShoppingCart());
        Customer customer = db.queryForObject("SELECT * FROM customers WHERE id = ?", new CustomerRowMapper(), id);
        Address address = db.queryForObject("SELECT * FROM addresses WHERE id = ?", new AddressRowMapper(),
                customer.getAddressID());
        model.addAttribute(address);
        model.addAttribute(customer);
        model.addAttribute("templateName", "customer_edit");
        model.addAttribute("title", "Benutzer ändern");
        model.addAttribute("cookiesAccepted", session.getCookiesAccepted());
        return "layout";
    }

    /**
     * save changes in customer data
     * 
     * @param response
     * @param customer
     * @param address
     * @param id
     * @param newPass
     * @return
     * @throws DataAccessException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/customer_edit{id}")
    public String customerEdited(@CookieValue(value = "SessionID", defaultValue = "") String sessID,
            HttpServletResponse response, @ModelAttribute Customer customer, @ModelAttribute Address address,
            @PathVariable String id, @ModelAttribute("newPass") String newPass)
            throws DataAccessException, NoSuchAlgorithmException {
        Session session = sessionController.getOrSetSession(sessID, response);
        String loggedInEmp = session.getLoggedInEmp();
        if (loggedInEmp.isEmpty()) {
            return "redirect:/";
        }
        Customer savedCust = db.queryForObject("SELECT * FROM customers WHERE id = ?", new CustomerRowMapper(), id);

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

        return "redirect:/customer_edit" + customer.getId().toString();
    }
}