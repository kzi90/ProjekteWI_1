package webshop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonController {

    @GetMapping("/")
    public String home(Model model) {
        Person person = new Person();
        person.setFirstname("Alfred");
        person.setLastname("Neumann");
        model.addAttribute(person);
        return "home";
    }

    @GetMapping("/register")
    public String register(Model model){
        Person person = new Person();
        model.addAttribute(person);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Person person, Model model){
        model.addAttribute(person); // womöglich überflüssig an dieser Stelle
        // hier sollte der user in der Datenbank gespeichert werden.
        return "registered";
    }

}