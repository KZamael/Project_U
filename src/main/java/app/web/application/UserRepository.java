package app.web.application;

import app.business.model.User;

/**
 * Repository implementation for work on {@link User} Documents in the MongoDB
 */
public class UserRepository extends AbstractRepository<User> {

    // this property provides the collection name where User Documents are stored
    public static final String COLLECTION_NAME = "users";

    /**
     * Constructor for a UserRepository.
     *
     * @param connection the MongoConnection instances that encapsules the MongoDB Database Connection
     * @throws ClassNotFoundException in case when User Class is not present at runtime
     */
    public UserRepository(MongoConnection connection) throws ClassNotFoundException {
        super(connection, COLLECTION_NAME);
    }

    /**
     * queries the collection 'qusers' for a User instance with the given username
     *
     * @param username of the User
     * @return the User instance with matching username
     */
    public User getUser(String username) {
        return find("username", username).get(0);
    }
}