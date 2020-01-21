package app.business.util;

import app.web.application.MongoConnection;
import app.web.application.PaymentgroupRepository;
import app.web.application.UserRepository;
import app.web.controller.PaymentgroupController;
import app.web.controller.SessionHandler;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder class for this application. created from a static call of 'create', it consumes a {@link IFactory}
 * that is used to instantiate Object Instances. All of this instances are put into a Map (objects)
 * and participants could lookup them via the 'getBean' method
 */
public class AppBuilder {

    private static AppBuilder _instance;
    private IFactory factory = null;
    private Map<String, Object> objects = new HashMap<>();


    /**
     * static create function (singleton)
     *
     * @return a {@link AppBuilder} instance
     */
    public static AppBuilder create() {
        if (_instance == null) {
            _instance = new AppBuilder();
        }
        return _instance;
    }

    /**
     * configures this builder with an IFactory instance
     *
     * @param factory used to construct object instances
     * @return this {@link AppBuilder} instance
     */
    public AppBuilder withFactory(IFactory factory) {
        this.factory = factory;
        return this;
    }

    /**
     * builder method that takes care of instantiation of objects (via call to factory methods)
     * and build up the object graph of the entire app (taking care of the exact order of creation and
     * compositing of aggregations).
     * created object instances are put into the 'objects' map
     *
     * @return the {@link AppBuilder} instance
     */
    public AppBuilder build() {
        MongoConnection con = new MongoConnection("projectU", "trainer", "sniebel");
        this.objects.put(MongoConnection.class.getSimpleName(), con);
        try {
            PaymentgroupRepository pgr = (PaymentgroupRepository) factory.createRepository(PaymentgroupRepository.COLLECTION_NAME, con);
            this.objects.put(PaymentgroupRepository.class.getSimpleName(), pgr);
            UserRepository qur = (UserRepository) factory.createRepository(UserRepository.COLLECTION_NAME, con);
            this.objects.put(UserRepository.class.getSimpleName(), qur);
            SessionHandler sh = factory.createSessionHandler(qur);
            this.objects.put(SessionHandler.class.getSimpleName(), sh);
            PaymentgroupController pgc = (PaymentgroupController) factory.createController("Paymentgroup", pgr, qur);
            this.objects.put(PaymentgroupController.class.getSimpleName(), pgc);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Javalin app = Javalin.create();
        this.objects.put(Javalin.class.getSimpleName(), app);
        AppConfiguration apc = new AppConfiguration(this);
        this.objects.put(AppConfiguration.class.getSimpleName(), apc);
        apc.configure();
        app.start();
        return this;
    }

    /**
     * retrieves an object from the 'objects' map by name.
     *
     * @param name of the object to be retrieved (key is Class.getSimpleName())
     * @return the object instance (singleton) requested (has to be casted to right Type)
     * @throws InstantiationException if the Object cannot be found by name
     */
    public Object getBean(String name) throws InstantiationException {
        Object toRet = objects.get(name);
        if (toRet == null) {
            throw new InstantiationException("bean with name " + name + " could not be found in app context");
        }
        return toRet;
    }
}
