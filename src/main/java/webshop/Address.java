package webshop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kasimir Eckhardt
 */
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Address {
    private Integer id;
    private String street;
    private String housenr;
    private String postcode;
    private String city;
    private String country;

    /**
     * compare addresses by street, housnr, postcode & city
     * @param address
     * @return
     */
    public boolean equalsAddress(Address address) {
        return this.street.equals(address.street) && this.housenr.equals(address.housenr)
                && this.postcode.equals(address.postcode) && this.city.equals(address.city);
    }

    @Override
    public String toString() {
        return String.format("ID: %d, %s %s, %s %s, %s", this.id, this.street, this.housenr, this.postcode, this.city,
                this.country);
    }
}