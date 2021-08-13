package webshop;

import java.security.NoSuchAlgorithmException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public abstract class Person {
    protected Integer id;
    protected String firstname;
    protected String lastname;
    protected String birthdate;
    protected Integer addressID;
    protected String email;
    protected String phonenumber;
    protected String passHash;

    /**
	 * Convert given password to SHA256-Hash and compare with saved Hash
	 * @param password
	 * @return boolean value that indicates whether the login was successful
	 * @throws NoSuchAlgorithmException
	 */
	public boolean login(String password) throws NoSuchAlgorithmException{
		return Convert.stringToHash(password).equalsIgnoreCase(this.passHash);
	}

}