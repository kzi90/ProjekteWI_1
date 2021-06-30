package webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
        Person person1 = jdbcTemplate.queryForObject("SELECT * FROM Person WHERE Id = ?;", new PersonRowMapper(), 1);
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
        String testSQL = "SELECT * FROM Person WHERE firstname = ? AND lastname = ?;";
        boolean alreadyRegistered = (!jdbcTemplate.query(testSQL, new PersonRowMapper(), person.getFirstname(), person.getLastname()).isEmpty());
        model.addAttribute("alreadyRegistered", alreadyRegistered);
        if (!alreadyRegistered){
            // id wird automatisch durch die Datenbank vergeben
            String saveSQL = "INSERT INTO person (firstname, lastname) VALUES (?, ?);";
            this.jdbcTemplate.update(saveSQL, person.getFirstname(), person.getLastname());
        }
        return "registered";
    }

}