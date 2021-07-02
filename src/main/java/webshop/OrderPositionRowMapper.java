package webshop;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class OrderPositionRowMapper implements RowMapper<OrderPosition> {
    
    public OrderPosition mapRow(ResultSet rs, int rowNum) throws SQLException{
        return OrderPosition.builder()
            .posNr(rs.getInt("pos_nr"))
            .quantity(rs.getInt("quantity"))
            .productID(rs.getInt("product_id"))
            .orderID(rs.getInt("order_id"))
            .build();
    }
}