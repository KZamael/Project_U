package app.web.application;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * This class encapsulates a {@link MongoClient} and provides access to the given Database.
 * It is responsible for managing all client configuration and connecting to the database which then could be used
 * by all {@link AbstractRepository} implemetations
 *
 * @see MongoClient
 */
public class MongoConnection {

    private MongoDatabase db;
    private MongoClient client;

    /**
     * Constructor for a MongoConnection. Configures the {@link MongoClient} and opens a Database-Connection.
     * It is assumed that the DB is access protected by a user that has readWrite Access Role to the given database.
     *
     * @param database the name of the database to work on. Used as authenticationDatabase as well
     * @param username the username for DB authentication
     * @param password the password for DB authentication
     */
    public MongoConnection(String database, String username, String password) {
        // registers Objects in our project to act as POJO Data-Transfer-Objects via a CodecRegistry (feature is used in repositories)
        CodecRegistry pRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        // builds Client settings by providing the CodecRegistry and the connection-string
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(pRegistry)
                .applyConnectionString(new ConnectionString("mongodb://" + username + ":" + password + "@localhost/?authSource=" + database))
                .build();
        // creates a client object and connects to the MongoDB
        client = MongoClients.create(settings);
        db = client.getDatabase(database);
    }

    /**
     * getter for the MongoDatabase this Object holds
     *
     * @return the {@link MongoDatabase} instance for data retrieval and manipulation
     */
    public MongoDatabase getConnection() {
        return db;
    }

    /**
     * closes the connection to the MongoDB.
     * Should be called when no DB-Connection is needed to gracefully disconnect from MongoDB
     */
    public void close() {
        client.close();
    }
}
