package webshop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor  // needed for getting form-inputs via POST and thymeleaf
public class Person {
    private Long id;
    private String firstname;
    private String lastname;
}