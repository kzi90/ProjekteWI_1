package webshop;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 
     * @param model
     * @return home.html template
     */
    @GetMapping("/")
    public String home(Model model) {
        Person person = Person.builder().firstname("Alfred").lastname("Neumann").build();
        model.addAttribute(person);
        Person person1 = jdbcTemplate.queryForObject("SELECT * FROM Person WHERE Id = ?", new PersonRowMapper(), 1);
        if (person1 != null){
            model.addAttribute(person1);
        }
        return "home";
    }
    /**
     * 
     * @param model
     * @return register.html template
     */
    @GetMapping("/register")
    public String register(Model model){
        Person person = Person.builder().build();
        model.addAttribute(person);
        return "register";
    }

    /**
     * writes the registered person into the database and shows success message
     * @param person built automatically with submitted values
     * @param model
     * @return registered.html template
     */
    @PostMapping("/register")
    public String registered(@ModelAttribute Person person, Model model){
        model.addAttribute(person);
        String testsql = "SELECT * FROM Person WHERE firstname = '%s' AND lastname = '%s';";
        if (jdbcTemplate.query(String.format(testsql, person.getFirstname(), person.getLastname()), new PersonRowMapper()).isEmpty()){
            String savesql = "INSERT INTO person (firstname, lastname) VALUES ('%s', '%s');";
            // id wird automatisch durch die Datenbank vergeben
            this.jdbcTemplate.execute(String.format(savesql, person.getFirstname(), person.getLastname()));
        }
        return "registered";
    }

}