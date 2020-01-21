package app.web.application;

import java.util.List;

/**
 * The base interface for all database related actions. defines data retrieval and save
 * operations for subclasses. in order to adopt to certain data-objects this interface is implemented as
 * Generic interface. Follows the "Data-Access-Object" Design-Pattern.
 * <p>
 * Subclasses are forced to give the concrete Type of a Data-Transfer-Object that are stored in a collection.
 * So every disparate DB-Document will have its own Repository Implementation
 *
 * @param <T> the Data-Transfer-Object type the repository should work on
 * @see <a href="https://docs.oracle.com/javase/tutorial/java/generics/types.html">Generics in Oracles java tutorial</a>
 * @see <a href="https://www.oracle.com/technetwork/java/dataaccessobject-138824.html">Oracle technet post realted to DAO design pattern</a>
 */
public interface IRepository<T> {

    /**
     * find all Data-Transfer-Objects of the corresponding Type
     *
     * @return a list of all T Objects in the Collection
     */
    List<T> findAll();

    /**
     * find the DTOs in the collection, where a given property has a certain value
     *
     * @param propertyName  on which the collection should be filtered
     * @param propertyValue that should be searched for (equality of given value)
     * @return a list of all T Objects in the Collection where the given propertyName has the propertyValue provided by the params
     */
    List<T> find(String propertyName, String propertyValue);

    /**
     * Inserts a new Document to a collection
     *
     * @param objToInsert the object that should be newly created in database
     */
    void create(T objToInsert);

    /**
     * updates an DTO of the Type T. in order to map the object to a DB Entity the corresponding Database Entity is
     * retrieved by a query for a specific property value
     *
     * @param propertyName  on which the filter should be applied. Should be a property where unique values are supplied
     * @param propertyValue that should be searched
     * @param objToSave     the Data Object that reqpresents the new DB-Entity State.
     */
    void save(String propertyName, String propertyValue, T objToSave);
}