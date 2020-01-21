package app.business.util;

import app.web.application.MongoConnection;
import app.web.controller.PaymentgroupController;
import app.web.controller.SessionHandler;

import io.javalin.Javalin;
import io.javalin.core.security.AccessManager;

import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * This Class uses the AppBuilder and takes care of configuration of the Javalin App Instance, namely
 * the routes and application lifecycle events.
 */
public class AppConfiguration {

    private AppBuilder builder;
    private Javalin app;


    /**
     * constructor. takes the AppBuilder and requests the Javalin Instance from it for further configuration.
     *
     * @param builder the appBuilder to work on
     */
    public AppConfiguration(AppBuilder builder) {
        this.builder = builder;
        try {
            this.app = ((Javalin) builder.getBean(Javalin.class.getSimpleName()));
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * configures Javalin
     *
     * @return The ready configured Javalin instance
     */
    public Javalin configure() {
        /*
         * marks the contents of the resources (copied over from src/main/resources/static) as static. This means, that
         * assets from this location are served by the embedded Webserver 'as is' without server-side processing
         */
        app.config.addStaticFiles("/static");
        app.config.addStaticFiles("/static/images");
        /*
         * configures the {@link SessionHandler} as {@link AccessManager} conforming to Javalins specs
         * @see https://javalin.io/documentation#access-manager
         */
        try {
            app.config.accessManager((AccessManager) builder.getBean(SessionHandler.class.getSimpleName()));
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        /*
         * configures all routes of the app. paths are aggregated with their included HTTP methods and every combination
         * gets a handler assigned by defining the appropriate controller methods built for this particular action.
         * @see https://javalin.io/documentation#handler-groups
         */
        app.routes(() -> {
            path("/", () -> {
                get((ctx) -> ((PaymentgroupController) builder.getBean(PaymentgroupController.class.getSimpleName())).handleGetGroups(ctx));
                post((ctx) -> ((PaymentgroupController) builder.getBean(PaymentgroupController.class.getSimpleName())).handleAddPaygroup(ctx));
            });
            path("/add_group", () -> get((ctx) -> ((PaymentgroupController) builder.getBean(PaymentgroupController.class.getSimpleName())).handleShowAddPaygroup(ctx)));
            path("/balance/:title", () -> {
                get((ctx) -> ((PaymentgroupController) builder.getBean(PaymentgroupController.class.getSimpleName())).handleGetPaygroup(ctx));
                path("trx", () -> {
                    get((ctx) -> ((PaymentgroupController) builder.getBean(PaymentgroupController.class.getSimpleName())).handleGetMoney(ctx));
                    post((ctx) -> ((PaymentgroupController) builder.getBean(PaymentgroupController.class.getSimpleName())).handleAddMoney(ctx));
                });
            });
            path("/login", () -> {
                get((ctx) -> ctx.render("/login.html"));
                post((ctx) -> ((SessionHandler) builder.getBean(SessionHandler.class.getSimpleName())).handleLogin(ctx));
            });
            path("/logout", () -> {
                get((ctx) -> ((SessionHandler) builder.getBean(SessionHandler.class.getSimpleName())).handleLogout(ctx));
            });
        });
        /*
         * event handlers give the opportunity to react to events of the application state
         * @see https://javalin.io/documentation#lifecycle-events
         */
        app.events(evt -> evt.serverStopping(() -> ((MongoConnection) builder.getBean(MongoConnection.class.getSimpleName())).close()));
        // finally return the configured Javalin instance back to caller
        return app;
    }
}
