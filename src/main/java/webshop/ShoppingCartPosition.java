package webshop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kasimir Eckhardt
 */
@Getter @Setter
@AllArgsConstructor // needed for getting form-inputs via POST and thymeleaf
@NoArgsConstructor
public class ShoppingCartPosition {
    private Integer productID;
    private Integer quantity;
}