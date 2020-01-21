package app.business.model;

/**
 * Data-Transfer-Object reflcting a 'quser' JSON Document in the MongoDB Collection 'qusers'.
 * Simple Bean with getters an setters of properties mapped to underlying DB Document
 * <p>
 * example of Documents mapped by this POJO:
 *
 * <pre>{@code
 * {
 *     "_id" : ObjectId("5dbe1ca6d7480aa333b3c22a"),
 *     "username" : "Homer",
 *     "password" : "HomerJ"
 * }
 * }</pre>
 *
 * @see <a href="https://mongodb.github.io/mongo-java-driver/3.12/bson/pojos/">POJO section in mongo java driver docs</a>
 */
public class User {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
