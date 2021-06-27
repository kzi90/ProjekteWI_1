package webshop;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class PersonRowMapper implements RowMapper<Person> {
    
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Person.builder()
            .id(rs.getInt("ID"))
            .firstname(rs.getString("firstname"))
            .lastname(rs.getString("lastname"))
            .build();
    }
}