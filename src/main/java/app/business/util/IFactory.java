package app.business.util;

import app.web.application.UserRepository;
import app.web.controller.AbstractController;
import app.web.controller.SessionHandler;
import app.web.application.IRepository;
import app.web.application.MongoConnection;

/**
 * Factory interface for creation of Object Instances (used by {@link AppBuilder}).
 */
public interface IFactory {
    /**
     * creates a Repository Instance by name
     *
     * @param name            of the Repository to create
     * @param mongoConnection the MongoConnection that is shared across all repositories
     * @return a concrete Instance that implements the {@link IRepository} instrface
     * @throws ClassNotFoundException if the Repository cannot be created (e.g. the name is not known)
     */
    @SuppressWarnings("rawtypes")
    IRepository createRepository(String name, MongoConnection mongoConnection) throws ClassNotFoundException;

    /**
     * creates a Controller Instance by name
     *
     * @param name of the Controller to create
     * @param repo one or more {@link IRepository} instances this controller can use to coordinate data access
     * @return an {@link AbstractController} subclass instance
     */
    @SuppressWarnings("rawtypes")
    AbstractController createController(String name, IRepository... repo);

    /**
     * returns a {@link SessionHandler} that is responsible for checks and coordination of WebSessions
     *
     * @param repo the {@link UserRepository} insatce used for lookups of app users for authentication
     * @return a {@link SessionHandler} instance
     */
    SessionHandler createSessionHandler(UserRepository repo);
}

