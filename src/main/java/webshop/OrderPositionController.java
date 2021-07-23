package webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class OrderPositionController {
    
    @Autowired
    private JdbcTemplate db;

    public void saveOrderPos(OrderPosition orderPosition){
        String saveOrderPosSQL = "INSERT INTO orderpositions (pos_nr, quantity, product_id, order_id) VALUES (?, ?, ?, ?);";
        this.db.update(saveOrderPosSQL, orderPosition.getPosNr(),
                                        orderPosition.getQuantity(),
                                        orderPosition.getProductID(),
                                        orderPosition.getOrderID());
    }

}