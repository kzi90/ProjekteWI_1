package webshop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Kasimir Eckhardt
 */
@Getter @Setter
@AllArgsConstructor // needed for getting form-inputs via POST and thymeleaf
@SuperBuilder
public class Customer extends Person {

    /**
     * compare customer by firstname, lastname, birthdate, email & phonenumber
     * @param customer
     * @return
     */
    public boolean equalsCust(Customer customer) {
        return this.firstname.equals(customer.firstname) && this.lastname.equals(customer.lastname)
                && this.birthdate.equals(customer.birthdate) && this.email.equals(customer.email)
                && this.phonenumber.equals(customer.phonenumber);
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Adress-ID: %d, %s %s, *%s, %s, %s, %s", this.id, this.addressID, this.firstname,
                this.lastname, this.birthdate, this.email, this.phonenumber, this.passHash);
    }
}