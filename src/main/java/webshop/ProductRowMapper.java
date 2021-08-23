package webshop;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author Kasimir Eckhardt
 */
public class ProductRowMapper implements RowMapper<Product> {

    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Product.builder()
                       .id(rs.getInt("ID"))
                       .type(rs.getString("product_type"))
                       .name(rs.getString("product_name"))
                       .description(rs.getString("product_description"))
                       .imgURL(rs.getString("image_url"))
                       .amount(rs.getInt("amount_ml"))
                       .price(rs.getDouble("price_eur"))
                       .active(rs.getBoolean("active"))
                       .build();
    }
}