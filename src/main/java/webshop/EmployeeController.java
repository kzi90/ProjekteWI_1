package webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class EmployeeController {
    
    @Autowired
    private JdbcTemplate db;

    // hier Methoden zum Speichern von Employees in der DB implementieren

}