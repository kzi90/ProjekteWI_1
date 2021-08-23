package webshop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Kasimir Eckhardt
 */
public class CustomerRowMapper implements RowMapper<Customer> {
    
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Customer.builder()
                       .id(rs.getInt("ID"))
                       .firstname(rs.getString("firstname"))
                       .lastname(rs.getString("lastname"))
                       .birthdate(new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("birthdate")))
                       .addressID(rs.getInt("address_id"))
                       .email(rs.getString("email"))
                       .phonenumber(rs.getString("phonenumber"))
                       .passHash(rs.getString("pass_hash"))
                       .active(rs.getBoolean("active"))
                       .build();
    }
}