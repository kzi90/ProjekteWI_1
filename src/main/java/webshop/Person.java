package webshop;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
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
	 * 
	 * @param password
	 * @return boolean value that indicates whether the login was successful
	 * @throws NoSuchAlgorithmException
	 */
	public boolean login(String password) throws NoSuchAlgorithmException{
		return Convert.stringToHash(password).equalsIgnoreCase(this.passHash);
	}

}