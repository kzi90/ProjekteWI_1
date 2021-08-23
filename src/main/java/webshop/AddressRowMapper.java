package webshop;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Kasimir Eckhardt
 */
public class AddressRowMapper implements RowMapper<Address> {
    
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Address.builder()
            .id(rs.getInt("ID"))
            .street(rs.getString("street"))
            .housenr(rs.getString("housenr"))
            .postcode(rs.getString("postcode"))
            .city(rs.getString("city"))
            .country(rs.getString("country"))
            .build();
    }
}