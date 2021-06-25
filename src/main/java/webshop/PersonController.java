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
    JdbcTemplate jdbcTemplate;

    /**
     * 
     * @param model
     * @return home.html template
     */
    @GetMapping("/")
    public String home(Model model) {
        Person person = Person.builder().firstname("Alfred").lastname("Neumann").build();
        model.addAttribute(person);
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
        String sql = String.format("INSERT INTO person VALUES (4, '%s', '%s');", person.getFirstname(), person.getLastname());
        this.jdbcTemplate.execute(sql);
        return "registered";
    }

}