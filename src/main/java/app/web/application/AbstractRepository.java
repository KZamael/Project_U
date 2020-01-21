package app.web.application;

import com.mongodb.client.MongoCollection;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

/**
 * Base Implementation of the {@link IRepository} interface. It keeps the generic types from the Interface
 * and thus is marked abstract (has to be extended at least in order to define the Data-Transfer_object type)
 * <p>
 * It implements data retrieval and manipulation mehtods of the {@link IRepository} interface
 * with a MongoDB Databases.
 *
 * @param <T> the Data-Transfer-Object type the repository should work on
 * @see <a href="https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html">Abstract Methods and classes in Oracles Java tutorial</a>
 * @see <a href="https://mongodb.github.io/mongo-java-driver/3.12/driver/">MongoDB Java driver docs</a>
 * @see <a href="https://mongodb.github.io/mongo-java-driver/3.12/bson/pojos/">POJO section in MongoDB Java driver docs</a>
 */
public abstract class AbstractRepository<T> implements IRepository<T> {

    protected MongoCollection<T> collection;

    /**
     * Constructor of a Repository. Takes all necessary collaborators as constructor argument
     *
     * @param connection     the MongoConnection that encapsules a {@link com.mongodb.client.MongoDatabase}
     * @param collectionName the collection name this repository should use to query for from the MongoDB
     * @throws ClassNotFoundException in case when the generic Type T provided by a subclass is not found at runtime
     */
    @SuppressWarnings("unchecked")
    protected AbstractRepository(MongoConnection connection, String collectionName) throws ClassNotFoundException {
        // the following two lines use Reflection in order to get the Runtime Class Name of the <T> Parameter as a string
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
        // retrieves a MongoCollection object by name and binds the DTO-Type to it in order to work with POJOs
        collection = (MongoCollection<T>) connection.getConnection().getCollection(collectionName, Class.forName(parameterClassName));
    }


    @Override
    public List<T> findAll() {
        List<T> toRet = new ArrayList<>();
        collection.find().forEach((Consumer<T>) toRet::add);
        return toRet;
    }

    @Override
    public List<T> find(String propertyName, String propertyValue) {
        List<T> toRet = new ArrayList<>();
        collection.find(eq(propertyName,propertyValue)).forEach((Consumer<T>) toRet::add);
        return toRet;
    }

    @Override
    public void save(String propertyName, String propertyValue,T objToSave) {
        collection.replaceOne(eq(propertyName,propertyValue),objToSave);
    }

    @Override
    public void create(T objToInsert) {
        collection.insertOne(objToInsert);
    }
}
