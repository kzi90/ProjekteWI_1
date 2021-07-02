package webshop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@AllArgsConstructor  // needed for getting form-inputs via POST and thymeleaf
@SuperBuilder
public class Employee extends Person {
    private String department;
    private Boolean isAdmin;
}