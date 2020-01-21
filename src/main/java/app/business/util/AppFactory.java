package app.business.util;

import app.web.application.IRepository;
import app.web.application.MongoConnection;
import app.web.application.PaymentgroupRepository;
import app.web.application.UserRepository;
import app.web.controller.AbstractController;
import app.web.controller.PaymentgroupController;
import app.web.controller.SessionHandler;

/**
 * Implementaion of the {@link IFactory} interface that takes care of creation of objects
 */
public class AppFactory implements IFactory {
    // static reference of this factory
    private static AppFactory _instance = null;

    /**
     * getter for this Singleton instance (creation on demand)
     *
     * @return an instance of this AppFactory Class
     */
    public static AppFactory getInstance() {
        if (_instance == null) {
            _instance = new AppFactory();
        }
        return _instance;
    }

    /**
     * evaluates the given name and calls the repository constructors (the collection name as parameter)
     *
     * @param name            of the Repository to create
     * @param mongoConnection the MongoConnection that is shared across all repositories
     * @return a subclass of {@link IRepository} with the shared {@link MongoConnection}
     * @throws ClassNotFoundException if the combination of name and mongoConnection could not be handled by this method
     */
    @SuppressWarnings("rawtypes")
    @Override
    public IRepository createRepository(String name, MongoConnection mongoConnection) throws ClassNotFoundException {
        if (name.equals(UserRepository.COLLECTION_NAME)) {
            return new UserRepository(mongoConnection);
        }
        if (name.equals(PaymentgroupRepository.COLLECTION_NAME)) {
            return new PaymentgroupRepository(mongoConnection);
        }
        throw new ClassNotFoundException();
    }

    /**
     * creates controllers and configures them with repositores to use for database interactions
     *
     * @param name of the Controller to create
     * @param repo one or more {@link IRepository} instances this controller can use to coordinate data access
     * @return the subclass of {@link AbstractController} tha corresponds to the given name
     */
    @SuppressWarnings("rawtypes")
    @Override
    public AbstractController createController(String name, IRepository... repo) {
        if (name.equals("Paymentgroup")) {
            return new PaymentgroupController((PaymentgroupRepository) repo[0], (UserRepository) repo[1]);
        }
        return null;
    }

    @Override
    public SessionHandler createSessionHandler(UserRepository repo) {
        return new SessionHandler(repo);
    }
}
