package webshop;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

/**
 * @author Kasimir Eckhardt
 */
@Getter @Setter
@AllArgsConstructor  // needed for getting form-inputs via POST and thymeleaf
@NoArgsConstructor
@Builder
public class Product {
    private Integer id;
    private String type;
    private String name;
    private String description;
    private Double alcContent;
    private String ingredients;
    private String imgURL;
    private Integer amount;
    private Double price;
    private Boolean active;
}