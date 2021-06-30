package webshop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor  // needed for getting form-inputs via POST and thymeleaf
@NoArgsConstructor
@Builder
public class Address {
    private Integer id;
    private String street;
    private String housenr;
    private String postcode;
    private String city;
    private String country;
}
