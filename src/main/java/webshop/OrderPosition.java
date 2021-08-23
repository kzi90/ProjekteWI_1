package webshop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kasimir Eckhardt
 */
@Getter @Setter
@AllArgsConstructor  // needed for getting form-inputs via POST and thymeleaf
@NoArgsConstructor
@Builder
public class OrderPosition {
    private Integer posNr;
    private Integer quantity;
    private Integer productID;
    private Integer orderID;
}