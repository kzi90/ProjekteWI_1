package webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController {
    
    @Autowired
    private JdbcTemplate db;

    // hier Methoden zum Speichern von Produkten in der DB implementieren

}