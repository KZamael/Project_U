package app.web.application;

import java.util.List;
import app.business.model.Paymentgroup;

/**
 * Repository implementation for work on {@link Paymentgroup} Documents in the MongoDB
 */
public class PaymentgroupRepository extends AbstractRepository<Paymentgroup> {

    // this property provides the collection name where Paymentgroup Documents are stored
    public static final String COLLECTION_NAME = "paymentgroups";

    /**
     * Constructor for a PaymentgroupRepository.
     *
     * @param connection the MongoConnection instances that encapsules the MongoDB Database Connection
     * @throws ClassNotFoundException in case when Paymentgroup Class is not present at runtime
     */
    public PaymentgroupRepository(MongoConnection connection) throws ClassNotFoundException {
        super(connection, COLLECTION_NAME);
    }

    /**
     * queries collection 'paygroups' for Paymentgroup instances where the given username is a member of
     *
     * @param username the username acting as search criterion
     * @return a list of paygroups where the User is member of
     */
    public List<Paymentgroup> getPaygroupsForUsername(String username) {
        return find("users", username);
    }

    /**
     * queries collection 'paygroups' for Paymentgroup instances with given title
     *
     * @param title the title of the paygroup that should be looked up
     * @return the paygroup with the given title
     */
    public Paymentgroup getPaygroupForTitle(String title) {
        return find("title", title).get(0);
    }
}