package webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {
    
    @Autowired
    private JdbcTemplate db;

    // hier Methoden zum Speichern von Orders in der DB implementieren

}