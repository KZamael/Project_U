package app.business.entities;

public class User {

    public final String userName;
    public final String hashedPassword;
    public final String salt;
    public final String firstName;
    public final String lastName;

    public User(String userName, String hashedPassword, String salt, String firstName, String lastName) {
        super();
        this.userName = userName;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
