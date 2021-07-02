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
public class Order {
    private Integer id;
    private String orderDate;
    private String orderTime;
    private Double totalPrice;
    private String status;
    private Integer custID;
}