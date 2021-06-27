package webshop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor  // needed for getting form-inputs via POST and thymeleaf
public class Address {
    private Integer id;
    private String street;
    private String housenr;
    private Integer postcode;
    private String city;
}
