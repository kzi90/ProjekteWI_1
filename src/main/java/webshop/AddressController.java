package webshop;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

/**
 * @author Kasimir Eckhardt
 */
@Controller
public class AddressController {
    
    @Autowired
    private JdbcTemplate db;

    /**
     * save address in database if it doesn't exist already
     * @param address
     * @return
     */
    public Address saveAddress(Address address){
        String testAddressSQL = "SELECT * FROM addresses WHERE street = ? AND housenr = ? AND postcode = ? AND city = ?;";
        List<Address> tAddresses = db.query(testAddressSQL, new AddressRowMapper(), address.getStreet(),
                                                                                    address.getHousenr(),
                                                                                    address.getPostcode(),
                                                                                    address.getCity());
        if (tAddresses.isEmpty()){
            String saveAddressSQL = "INSERT INTO addresses (street, housenr, postcode, city) VALUES (?, ?, ?, ?);";
            db.update(saveAddressSQL, address.getStreet(),
                                           address.getHousenr(),
                                           address.getPostcode(),
                                           address.getCity());
            address = db.queryForObject(testAddressSQL, new AddressRowMapper(), address.getStreet(),
                                                                                address.getHousenr(),
                                                                                address.getPostcode(),
                                                                                address.getCity());
        } else {
            address = tAddresses.get(0);
        }
        return address;
    }
}