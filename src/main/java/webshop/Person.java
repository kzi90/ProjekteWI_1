package webshop;

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
    protected Integer addressID;
    protected String email;
    protected String phonenumber;
    protected String passHash;
}