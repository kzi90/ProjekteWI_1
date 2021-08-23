package webshop;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Kasimir Eckhardt
 */
@Controller
public class EmployeeController {

    @Autowired
    private JdbcTemplate db;

    @Autowired
    private AddressController addressController;

    /**
     * secret login for employees
     * 
     * @param loggedInUser
     * @param model
     * @return s3cr3tl0g1n.html template
     */
    @GetMapping("/s3cr3tl0g1n")
    public String s3cr3tl0g1n(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        Employee employee = new Employee();
        model.addAttribute(employee);
        model.addAttribute("templateName", "s3cr3tl0g1n");
        return "layout";
    }

    /**
     * trying employee login with submitted data
     * 
     * @param employee
     * @param response
     * @param model
     * @return redirects to /sortiment if login successful, else to /loginfail
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/s3cr3tl0g1n")
    public String loggedin(@ModelAttribute Employee employee, HttpServletResponse response, Model model)
            throws NoSuchAlgorithmException {
        List<Employee> employees = db.query("SELECT * FROM employees WHERE email = ?", new EmployeeRowMapper(),
                employee.getEmail());
        // employee.passHash contains the plain text password right now!
        if (!employees.isEmpty() && employees.get(0).login(employee.getPassHash())) {
            Cookie cookie = new Cookie("loggedInEmp", employee.getEmail());
            response.addCookie(cookie);
            return "redirect:/employee_area";
        } else {
            return "redirect:/loginfail";
        }
    }

    /**
     * logout from employee account
     * 
     * @param loggedInUser
     * @param response
     * @param model
     * @return logout.html template (same as for customers)
     */
    @GetMapping("/s3cr3tl0g0ut")
    public String s3cr3tl0g0ut(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        Cookie cookie = new Cookie("loggedInEmp", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        model.addAttribute("templateName", "s3cr3tl0g0ut");
        return "layout";
    }

    /**
     * 
     * @param loggedInUser
     * @param sessID
     * @param response
     * @param loggedInEmp
     * @param model
     * @return employee_area.html template
     */
    @GetMapping("/employee_area")
    public String employee_area(@CookieValue(value = "loggedInUser", defaultValue = "") String loggedInUser,
            @CookieValue(value = "SessionID", defaultValue = "") String sessID, HttpServletResponse response,
            @CookieValue(value = "loggedInEmp", defaultValue = "") String loggedInEmp, Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        ShoppingCart shoppingCart = ShoppingCartController.getShoppingCart(sessID, response);
        model.addAttribute("shoppingcart", shoppingCart);
        if (loggedInEmp.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("loggedInEmp", loggedInEmp);
        model.addAttribute("title", "Mitarbeiterbereich");
        model.addAttribute("templateName", "employee_area");
        return "layout";
    }

    /**
     * save employee- and addressdata if not already existing in database
     * 
     * @param employee
     * @param address
     * @return true if the email of the employee was not used before, else false
     * @throws DataAccessException
     * @throws ParseException
     * @throws NoSuchAlgorithmException
     */
    public boolean saveEmpWithAddress(Employee employee, Address address)
            throws DataAccessException, ParseException, NoSuchAlgorithmException {
        String testSQL = "SELECT * FROM employees WHERE email = ?;";
        boolean emailNotRegisteredBefore = db.query(testSQL, new EmployeeRowMapper(), employee.getEmail()).isEmpty();
        if (emailNotRegisteredBefore) {
            address = addressController.saveAddress(address);
            // database generates id automatically
            String saveSQL = "INSERT INTO customers (firstname, lastname, birthdate, "
                    + "address_id, email, phonenumber, pass_hash, department, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            this.db.update(saveSQL, employee.getFirstname(), employee.getLastname(),
                    // database works correct with String in this format, but not with type Date.
                    new SimpleDateFormat("yyyy-MM-dd")
                            .format(new SimpleDateFormat("dd.MM.yyyy").parse(employee.getBirthdate())),
                    address.getId(), employee.getEmail(), employee.getPhonenumber(),
                    // employee.passHash contains plain text password right now
                    Convert.stringToHash(employee.getPassHash()), employee.getDepartment(), employee.getIsAdmin());
        }
        return emailNotRegisteredBefore;
    }

}