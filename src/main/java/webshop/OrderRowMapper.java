package webshop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.springframework.jdbc.core.RowMapper;

public class OrderRowMapper implements RowMapper<Order> {
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException{
        return Order.builder()
            .id(rs.getInt("ID"))
            .orderDate(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("order_date")))
            .orderTime(new SimpleDateFormat("HH:mm:ss").format(rs.getTime("order_time")))
            .totalPrice(rs.getDouble("total_price"))
            .status(rs.getString("order_status"))
            .custID(rs.getInt("cust_id"))
            .build();
    }
}