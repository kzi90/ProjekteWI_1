package webshop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.springframework.jdbc.core.RowMapper;

public class EmployeeRowMapper implements RowMapper<Employee> {
    
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException{
        return Employee.builder()
                        .id(rs.getInt("ID"))
                        .firstname(rs.getString("firstname"))
                        .lastname(rs.getString("lastname"))
                        .birthdate(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("birthdate")))
                        .addressID(rs.getInt("address_id"))
                        .email(rs.getString("email"))
                        .phonenumber(rs.getString("phonenumber"))
                        .department(rs.getString("department"))
                        .isAdmin(rs.getBoolean("is_admin"))
                        .passHash(rs.getString("pass_hash"))
                        .active(true) // active-flag not saved in database
                        .build();
    }
}