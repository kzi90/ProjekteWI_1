package webshop;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class EmployeeController {
    
    @Autowired
    private JdbcTemplate db;

    @Autowired
    private AddressController addressController;

    public boolean saveEmpWithAddress(Employee employee, Address address) throws DataAccessException,
                                                                                 ParseException,
                                                                                 NoSuchAlgorithmException{
        String testSQL = "SELECT * FROM employees WHERE email = ?;";
        boolean emailNotRegisteredBefore = db.query(testSQL, new EmployeeRowMapper(), employee.getEmail()).isEmpty();
        if (emailNotRegisteredBefore){
            address = addressController.saveAddress(address);
            // database generates id automatically
            String saveSQL = "INSERT INTO customers (firstname, lastname, birthdate, " +
                             "address_id, email, phonenumber, pass_hash, department, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            this.db.update(saveSQL, employee.getFirstname(),
                                    employee.getLastname(),
                                    // database works correct with String in this format, but not with type Date.
                                    new SimpleDateFormat("yyyy-MM-dd").format(
                                        new SimpleDateFormat("dd.MM.yyyy").parse(employee.getBirthdate())),
                                    address.getId(),
                                    employee.getEmail(),
                                    employee.getPhonenumber(),
                                    // employee.passHash contains plain text password right now
                                    Convert.stringToHash(employee.getPassHash()),
                                    employee.getDepartment(),
                                    employee.getIsAdmin());
        }
        return emailNotRegisteredBefore;
    }

}