package webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JDBCTemplateController {
    // @Autowired
    // JdbcTemplate jdbcTemplate;
    // @GetMapping("/{id}")
    // @ResponseBody
    // public String home(@PathVariable Long id) {
    //     Person person = jdbcTemplate.queryForObject("SELECT * FROM Person WHERE Id = ?", new PersonRowMapper(), new Object[] {id});
    //     return person.toString();
    // }
}